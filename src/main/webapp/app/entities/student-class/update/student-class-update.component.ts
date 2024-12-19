import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMajor } from 'app/entities/major/major.model';
import { MajorService } from 'app/entities/major/service/major.service';
import { IStudentClass } from '../student-class.model';
import { StudentClassService } from '../service/student-class.service';
import { StudentClassFormGroup, StudentClassFormService } from './student-class-form.service';

@Component({
  standalone: true,
  selector: 'jhi-student-class-update',
  templateUrl: './student-class-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StudentClassUpdateComponent implements OnInit {
  isSaving = false;
  studentClass: IStudentClass | null = null;

  majorsSharedCollection: IMajor[] = [];

  protected studentClassService = inject(StudentClassService);
  protected studentClassFormService = inject(StudentClassFormService);
  protected majorService = inject(MajorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StudentClassFormGroup = this.studentClassFormService.createStudentClassFormGroup();

  compareMajor = (o1: IMajor | null, o2: IMajor | null): boolean => this.majorService.compareMajor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ studentClass }) => {
      this.studentClass = studentClass;
      if (studentClass) {
        this.updateForm(studentClass);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const studentClass = this.studentClassFormService.getStudentClass(this.editForm);
    if (studentClass.id !== null) {
      this.subscribeToSaveResponse(this.studentClassService.update(studentClass));
    } else {
      this.subscribeToSaveResponse(this.studentClassService.create(studentClass));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudentClass>>): void {
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

  protected updateForm(studentClass: IStudentClass): void {
    this.studentClass = studentClass;
    this.studentClassFormService.resetForm(this.editForm, studentClass);

    this.majorsSharedCollection = this.majorService.addMajorToCollectionIfMissing<IMajor>(this.majorsSharedCollection, studentClass.major);
  }

  protected loadRelationshipsOptions(): void {
    this.majorService
      .query()
      .pipe(map((res: HttpResponse<IMajor[]>) => res.body ?? []))
      .pipe(map((majors: IMajor[]) => this.majorService.addMajorToCollectionIfMissing<IMajor>(majors, this.studentClass?.major)))
      .subscribe((majors: IMajor[]) => (this.majorsSharedCollection = majors));
  }
}
