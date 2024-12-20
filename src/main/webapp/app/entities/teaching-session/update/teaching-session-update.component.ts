import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITimetable } from 'app/entities/timetable/timetable.model';
import { TimetableService } from 'app/entities/timetable/service/timetable.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { TeachingSessionService } from '../service/teaching-session.service';
import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionFormGroup, TeachingSessionFormService } from './teaching-session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-teaching-session-update',
  templateUrl: './teaching-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeachingSessionUpdateComponent implements OnInit {
  isSaving = false;
  teachingSession: ITeachingSession | null = null;

  timetablesSharedCollection: ITimetable[] = [];
  studentClassesSharedCollection: IStudentClass[] = [];
  classroomsSharedCollection: IClassroom[] = [];

  protected teachingSessionService = inject(TeachingSessionService);
  protected teachingSessionFormService = inject(TeachingSessionFormService);
  protected timetableService = inject(TimetableService);
  protected studentClassService = inject(StudentClassService);
  protected classroomService = inject(ClassroomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeachingSessionFormGroup = this.teachingSessionFormService.createTeachingSessionFormGroup();

  compareTimetable = (o1: ITimetable | null, o2: ITimetable | null): boolean => this.timetableService.compareTimetable(o1, o2);

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

  compareClassroom = (o1: IClassroom | null, o2: IClassroom | null): boolean => this.classroomService.compareClassroom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teachingSession }) => {
      this.teachingSession = teachingSession;
      if (teachingSession) {
        this.updateForm(teachingSession);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teachingSession = this.teachingSessionFormService.getTeachingSession(this.editForm);
    if (teachingSession.id !== null) {
      this.subscribeToSaveResponse(this.teachingSessionService.update(teachingSession));
    } else {
      this.subscribeToSaveResponse(this.teachingSessionService.create(teachingSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeachingSession>>): void {
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

  protected updateForm(teachingSession: ITeachingSession): void {
    this.teachingSession = teachingSession;
    this.teachingSessionFormService.resetForm(this.editForm, teachingSession);

    this.timetablesSharedCollection = this.timetableService.addTimetableToCollectionIfMissing<ITimetable>(
      this.timetablesSharedCollection,
      teachingSession.timetable,
    );
    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      teachingSession.studentClass,
    );
    this.classroomsSharedCollection = this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(
      this.classroomsSharedCollection,
      teachingSession.classroom,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.timetableService
      .query()
      .pipe(map((res: HttpResponse<ITimetable[]>) => res.body ?? []))
      .pipe(
        map((timetables: ITimetable[]) =>
          this.timetableService.addTimetableToCollectionIfMissing<ITimetable>(timetables, this.teachingSession?.timetable),
        ),
      )
      .subscribe((timetables: ITimetable[]) => (this.timetablesSharedCollection = timetables));

    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(studentClasses, this.teachingSession?.studentClass),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));

    this.classroomService
      .query()
      .pipe(map((res: HttpResponse<IClassroom[]>) => res.body ?? []))
      .pipe(
        map((classrooms: IClassroom[]) =>
          this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(classrooms, this.teachingSession?.classroom),
        ),
      )
      .subscribe((classrooms: IClassroom[]) => (this.classroomsSharedCollection = classrooms));
  }
}
