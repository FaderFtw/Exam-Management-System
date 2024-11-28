import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';
import { TeachingSessionService } from 'app/entities/teaching-session/service/teaching-session.service';
import { StudentClassService } from '../service/student-class.service';
import { IStudentClass } from '../student-class.model';
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

  examsSharedCollection: IExam[] = [];
  teachingSessionsSharedCollection: ITeachingSession[] = [];

  protected studentClassService = inject(StudentClassService);
  protected studentClassFormService = inject(StudentClassFormService);
  protected examService = inject(ExamService);
  protected teachingSessionService = inject(TeachingSessionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StudentClassFormGroup = this.studentClassFormService.createStudentClassFormGroup();

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  compareTeachingSession = (o1: ITeachingSession | null, o2: ITeachingSession | null): boolean =>
    this.teachingSessionService.compareTeachingSession(o1, o2);

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

    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(this.examsSharedCollection, studentClass.exam);
    this.teachingSessionsSharedCollection = this.teachingSessionService.addTeachingSessionToCollectionIfMissing<ITeachingSession>(
      this.teachingSessionsSharedCollection,
      studentClass.teachingSession,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(map((exams: IExam[]) => this.examService.addExamToCollectionIfMissing<IExam>(exams, this.studentClass?.exam)))
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));

    this.teachingSessionService
      .query()
      .pipe(map((res: HttpResponse<ITeachingSession[]>) => res.body ?? []))
      .pipe(
        map((teachingSessions: ITeachingSession[]) =>
          this.teachingSessionService.addTeachingSessionToCollectionIfMissing<ITeachingSession>(
            teachingSessions,
            this.studentClass?.teachingSession,
          ),
        ),
      )
      .subscribe((teachingSessions: ITeachingSession[]) => (this.teachingSessionsSharedCollection = teachingSessions));
  }
}
