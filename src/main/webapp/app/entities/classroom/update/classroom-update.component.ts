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
import { ClassroomService } from '../service/classroom.service';
import { IClassroom } from '../classroom.model';
import { ClassroomFormGroup, ClassroomFormService } from './classroom-form.service';

@Component({
  standalone: true,
  selector: 'jhi-classroom-update',
  templateUrl: './classroom-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClassroomUpdateComponent implements OnInit {
  isSaving = false;
  classroom: IClassroom | null = null;

  examsSharedCollection: IExam[] = [];
  teachingSessionsSharedCollection: ITeachingSession[] = [];

  protected classroomService = inject(ClassroomService);
  protected classroomFormService = inject(ClassroomFormService);
  protected examService = inject(ExamService);
  protected teachingSessionService = inject(TeachingSessionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClassroomFormGroup = this.classroomFormService.createClassroomFormGroup();

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  compareTeachingSession = (o1: ITeachingSession | null, o2: ITeachingSession | null): boolean =>
    this.teachingSessionService.compareTeachingSession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classroom }) => {
      this.classroom = classroom;
      if (classroom) {
        this.updateForm(classroom);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classroom = this.classroomFormService.getClassroom(this.editForm);
    if (classroom.id !== null) {
      this.subscribeToSaveResponse(this.classroomService.update(classroom));
    } else {
      this.subscribeToSaveResponse(this.classroomService.create(classroom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassroom>>): void {
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

  protected updateForm(classroom: IClassroom): void {
    this.classroom = classroom;
    this.classroomFormService.resetForm(this.editForm, classroom);

    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(this.examsSharedCollection, classroom.exam);
    this.teachingSessionsSharedCollection = this.teachingSessionService.addTeachingSessionToCollectionIfMissing<ITeachingSession>(
      this.teachingSessionsSharedCollection,
      classroom.teachingSession,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(map((exams: IExam[]) => this.examService.addExamToCollectionIfMissing<IExam>(exams, this.classroom?.exam)))
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));

    this.teachingSessionService
      .query()
      .pipe(map((res: HttpResponse<ITeachingSession[]>) => res.body ?? []))
      .pipe(
        map((teachingSessions: ITeachingSession[]) =>
          this.teachingSessionService.addTeachingSessionToCollectionIfMissing<ITeachingSession>(
            teachingSessions,
            this.classroom?.teachingSession,
          ),
        ),
      )
      .subscribe((teachingSessions: ITeachingSession[]) => (this.teachingSessionsSharedCollection = teachingSessions));
  }
}
