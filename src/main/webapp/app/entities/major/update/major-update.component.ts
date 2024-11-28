import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
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

  studentClassesSharedCollection: IStudentClass[] = [];

  protected majorService = inject(MajorService);
  protected majorFormService = inject(MajorFormService);
  protected studentClassService = inject(StudentClassService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MajorFormGroup = this.majorFormService.createMajorFormGroup();

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

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

    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      major.studentClass,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(studentClasses, this.major?.studentClass),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));
  }
}
