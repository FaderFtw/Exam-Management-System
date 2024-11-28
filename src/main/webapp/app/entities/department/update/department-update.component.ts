import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { IMajor } from 'app/entities/major/major.model';
import { MajorService } from 'app/entities/major/service/major.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
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

  usersSharedCollection: IUser[] = [];
  examSessionsSharedCollection: IExamSession[] = [];
  classroomsSharedCollection: IClassroom[] = [];
  majorsSharedCollection: IMajor[] = [];
  reportsSharedCollection: IReport[] = [];

  protected departmentService = inject(DepartmentService);
  protected departmentFormService = inject(DepartmentFormService);
  protected userService = inject(UserService);
  protected examSessionService = inject(ExamSessionService);
  protected classroomService = inject(ClassroomService);
  protected majorService = inject(MajorService);
  protected reportService = inject(ReportService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartmentFormGroup = this.departmentFormService.createDepartmentFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareExamSession = (o1: IExamSession | null, o2: IExamSession | null): boolean => this.examSessionService.compareExamSession(o1, o2);

  compareClassroom = (o1: IClassroom | null, o2: IClassroom | null): boolean => this.classroomService.compareClassroom(o1, o2);

  compareMajor = (o1: IMajor | null, o2: IMajor | null): boolean => this.majorService.compareMajor(o1, o2);

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, department.users);
    this.examSessionsSharedCollection = this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(
      this.examSessionsSharedCollection,
      ...(department.examSessions ?? []),
    );
    this.classroomsSharedCollection = this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(
      this.classroomsSharedCollection,
      department.classroom,
    );
    this.majorsSharedCollection = this.majorService.addMajorToCollectionIfMissing<IMajor>(this.majorsSharedCollection, department.major);
    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      department.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.department?.users)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.examSessionService
      .query()
      .pipe(map((res: HttpResponse<IExamSession[]>) => res.body ?? []))
      .pipe(
        map((examSessions: IExamSession[]) =>
          this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(examSessions, ...(this.department?.examSessions ?? [])),
        ),
      )
      .subscribe((examSessions: IExamSession[]) => (this.examSessionsSharedCollection = examSessions));

    this.classroomService
      .query()
      .pipe(map((res: HttpResponse<IClassroom[]>) => res.body ?? []))
      .pipe(
        map((classrooms: IClassroom[]) =>
          this.classroomService.addClassroomToCollectionIfMissing<IClassroom>(classrooms, this.department?.classroom),
        ),
      )
      .subscribe((classrooms: IClassroom[]) => (this.classroomsSharedCollection = classrooms));

    this.majorService
      .query()
      .pipe(map((res: HttpResponse<IMajor[]>) => res.body ?? []))
      .pipe(map((majors: IMajor[]) => this.majorService.addMajorToCollectionIfMissing<IMajor>(majors, this.department?.major)))
      .subscribe((majors: IMajor[]) => (this.majorsSharedCollection = majors));

    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.department?.report)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
