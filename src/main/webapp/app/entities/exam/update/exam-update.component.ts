import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { ExamService } from '../service/exam.service';
import { IExam } from '../exam.model';
import { ExamFormGroup, ExamFormService } from './exam-form.service';

@Component({
  standalone: true,
  selector: 'jhi-exam-update',
  templateUrl: './exam-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExamUpdateComponent implements OnInit {
  isSaving = false;
  exam: IExam | null = null;

  classroomsSharedCollection: IClassroom[] = [];
  studentClassesSharedCollection: IStudentClass[] = [];
  examSessionsSharedCollection: IExamSession[] = [];
  professorDetailsSharedCollection: IProfessorDetails[] = [];

  protected examService = inject(ExamService);
  protected examFormService = inject(ExamFormService);
  protected classroomService = inject(ClassroomService);
  protected studentClassService = inject(StudentClassService);
  protected examSessionService = inject(ExamSessionService);
  protected professorDetailsService = inject(ProfessorDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExamFormGroup = this.examFormService.createExamFormGroup();

  compareClassroom = (o1: IClassroom | null, o2: IClassroom | null): boolean => this.classroomService.compareClassroom(o1, o2);

  compareStudentClass = (o1: IStudentClass | null, o2: IStudentClass | null): boolean =>
    this.studentClassService.compareStudentClass(o1, o2);

  compareExamSession = (o1: IExamSession | null, o2: IExamSession | null): boolean => this.examSessionService.compareExamSession(o1, o2);

  compareProfessorDetails = (o1: IProfessorDetails | null, o2: IProfessorDetails | null): boolean =>
    this.professorDetailsService.compareProfessorDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exam }) => {
      this.exam = exam;
      if (exam) {
        this.updateForm(exam);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exam = this.examFormService.getExam(this.editForm);
    if (exam.id !== null) {
      this.subscribeToSaveResponse(this.examService.update(exam));
    } else {
      this.subscribeToSaveResponse(this.examService.create(exam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>): void {
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

  protected updateForm(exam: IExam): void {
    this.exam = exam;
    this.examFormService.resetForm(this.editForm, exam);

    this.classroomsSharedCollection = this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(
      this.classroomsSharedCollection,
      exam.classroom,
    );
    this.studentClassesSharedCollection = this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(
      this.studentClassesSharedCollection,
      exam.studentClass,
    );
    this.examSessionsSharedCollection = this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(
      this.examSessionsSharedCollection,
      exam.session,
    );
    this.professorDetailsSharedCollection = this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
      this.professorDetailsSharedCollection,
      ...(exam.supervisors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classroomService
      .query()
      .pipe(map((res: HttpResponse<IClassroom[]>) => res.body ?? []))
      .pipe(
        map((classrooms: IClassroom[]) =>
          this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(classrooms, this.exam?.classroom),
        ),
      )
      .subscribe((classrooms: IClassroom[]) => (this.classroomsSharedCollection = classrooms));

    this.studentClassService
      .query()
      .pipe(map((res: HttpResponse<IStudentClass[]>) => res.body ?? []))
      .pipe(
        map((studentClasses: IStudentClass[]) =>
          this.studentClassService.addStudentClassToCollectionIfMissing<IStudentClass>(studentClasses, this.exam?.studentClass),
        ),
      )
      .subscribe((studentClasses: IStudentClass[]) => (this.studentClassesSharedCollection = studentClasses));

    this.examSessionService
      .query()
      .pipe(map((res: HttpResponse<IExamSession[]>) => res.body ?? []))
      .pipe(
        map((examSessions: IExamSession[]) =>
          this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(examSessions, this.exam?.session),
        ),
      )
      .subscribe((examSessions: IExamSession[]) => (this.examSessionsSharedCollection = examSessions));

    this.professorDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfessorDetails[]>) => res.body ?? []))
      .pipe(
        map((professorDetails: IProfessorDetails[]) =>
          this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
            professorDetails,
            ...(this.exam?.supervisors ?? []),
          ),
        ),
      )
      .subscribe((professorDetails: IProfessorDetails[]) => (this.professorDetailsSharedCollection = professorDetails));
  }
}
