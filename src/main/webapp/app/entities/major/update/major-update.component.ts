import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IMajor } from '../major.model';
import { MajorService } from '../service/major.service';
import { MajorFormGroup, MajorFormService } from './major-form.service';

@Component({
  standalone: true,
  selector: 'jhi-major-update',
  templateUrl: './major-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MajorUpdateComponent implements OnInit {
  isSaving = false;
  major: IMajor | null = null;

  departmentsSharedCollection: IDepartment[] = [];

  protected majorService = inject(MajorService);
  protected majorFormService = inject(MajorFormService);
  protected departmentService = inject(DepartmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MajorFormGroup = this.majorFormService.createMajorFormGroup();

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ major }) => {
      this.major = major;
      if (major) {
        this.updateForm(major);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const major = this.majorFormService.getMajor(this.editForm);
    if (major.id !== null) {
      this.subscribeToSaveResponse(this.majorService.update(major));
    } else {
      this.subscribeToSaveResponse(this.majorService.create(major));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMajor>>): void {
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

  protected updateForm(major: IMajor): void {
    this.major = major;
    this.majorFormService.resetForm(this.editForm, major);

    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      major.department,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.major?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }
}
