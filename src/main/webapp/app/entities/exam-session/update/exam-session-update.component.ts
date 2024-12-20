import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISessionType } from 'app/entities/session-type/session-type.model';
import { SessionTypeService } from 'app/entities/session-type/service/session-type.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { ExamSessionService } from '../service/exam-session.service';
import { IExamSession } from '../exam-session.model';
import { ExamSessionFormGroup, ExamSessionFormService } from './exam-session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-exam-session-update',
  templateUrl: './exam-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExamSessionUpdateComponent implements OnInit {
  isSaving = false;
  examSession: IExamSession | null = null;

  sessionTypesCollection: ISessionType[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  protected examSessionService = inject(ExamSessionService);
  protected examSessionFormService = inject(ExamSessionFormService);
  protected sessionTypeService = inject(SessionTypeService);
  protected departmentService = inject(DepartmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExamSessionFormGroup = this.examSessionFormService.createExamSessionFormGroup();

  compareSessionType = (o1: ISessionType | null, o2: ISessionType | null): boolean => this.sessionTypeService.compareSessionType(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ examSession }) => {
      this.examSession = examSession;
      if (examSession) {
        this.updateForm(examSession);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const examSession = this.examSessionFormService.getExamSession(this.editForm);
    if (examSession.id !== null) {
      this.subscribeToSaveResponse(this.examSessionService.update(examSession));
    } else {
      this.subscribeToSaveResponse(this.examSessionService.create(examSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExamSession>>): void {
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

  protected updateForm(examSession: IExamSession): void {
    this.examSession = examSession;
    this.examSessionFormService.resetForm(this.editForm, examSession);

    this.sessionTypesCollection = this.sessionTypeService.addSessionTypeToCollectionIfMissing<ISessionType>(
      this.sessionTypesCollection,
      examSession.sessionType,
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      ...(examSession.departments ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sessionTypeService
      .query({ filter: 'examsession-is-null' })
      .pipe(map((res: HttpResponse<ISessionType[]>) => res.body ?? []))
      .pipe(
        map((sessionTypes: ISessionType[]) =>
          this.sessionTypeService.addSessionTypeToCollectionIfMissing<ISessionType>(sessionTypes, this.examSession?.sessionType),
        ),
      )
      .subscribe((sessionTypes: ISessionType[]) => (this.sessionTypesCollection = sessionTypes));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, ...(this.examSession?.departments ?? [])),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }
}
