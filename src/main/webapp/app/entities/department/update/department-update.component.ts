import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { DepartmentService } from '../service/department.service';
import { IDepartment } from '../department.model';
import { DepartmentFormGroup, DepartmentFormService } from './department-form.service';

@Component({
  standalone: true,
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;
  department: IDepartment | null = null;

  institutesSharedCollection: IInstitute[] = [];
  examSessionsSharedCollection: IExamSession[] = [];

  protected departmentService = inject(DepartmentService);
  protected departmentFormService = inject(DepartmentFormService);
  protected instituteService = inject(InstituteService);
  protected examSessionService = inject(ExamSessionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartmentFormGroup = this.departmentFormService.createDepartmentFormGroup();

  compareInstitute = (o1: IInstitute | null, o2: IInstitute | null): boolean => this.instituteService.compareInstitute(o1, o2);

  compareExamSession = (o1: IExamSession | null, o2: IExamSession | null): boolean => this.examSessionService.compareExamSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.department = department;
      if (department) {
        this.updateForm(department);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.departmentFormService.getDepartment(this.editForm);
    if (department.id !== null) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(department: IDepartment): void {
    this.department = department;
    this.departmentFormService.resetForm(this.editForm, department);

    this.institutesSharedCollection = this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(
      this.institutesSharedCollection,
      department.institute,
    );
    this.examSessionsSharedCollection = this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(
      this.examSessionsSharedCollection,
      ...(department.examSessions ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instituteService
      .query()
      .pipe(map((res: HttpResponse<IInstitute[]>) => res.body ?? []))
      .pipe(
        map((institutes: IInstitute[]) =>
          this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(institutes, this.department?.institute),
        ),
      )
      .subscribe((institutes: IInstitute[]) => (this.institutesSharedCollection = institutes));

    this.examSessionService
      .query()
      .pipe(map((res: HttpResponse<IExamSession[]>) => res.body ?? []))
      .pipe(
        map((examSessions: IExamSession[]) =>
          this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(examSessions, ...(this.department?.examSessions ?? [])),
        ),
      )
      .subscribe((examSessions: IExamSession[]) => (this.examSessionsSharedCollection = examSessions));
  }
}
