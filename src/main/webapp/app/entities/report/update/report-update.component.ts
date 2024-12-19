import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { ReportService } from '../service/report.service';
import { IReport } from '../report.model';
import { ReportFormGroup, ReportFormService } from './report-form.service';

@Component({
  standalone: true,
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;
  report: IReport | null = null;

  professorDetailsSharedCollection: IProfessorDetails[] = [];
  examSessionsSharedCollection: IExamSession[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  institutesSharedCollection: IInstitute[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected reportService = inject(ReportService);
  protected reportFormService = inject(ReportFormService);
  protected professorDetailsService = inject(ProfessorDetailsService);
  protected examSessionService = inject(ExamSessionService);
  protected departmentService = inject(DepartmentService);
  protected instituteService = inject(InstituteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReportFormGroup = this.reportFormService.createReportFormGroup();

  compareProfessorDetails = (o1: IProfessorDetails | null, o2: IProfessorDetails | null): boolean =>
    this.professorDetailsService.compareProfessorDetails(o1, o2);

  compareExamSession = (o1: IExamSession | null, o2: IExamSession | null): boolean => this.examSessionService.compareExamSession(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareInstitute = (o1: IInstitute | null, o2: IInstitute | null): boolean => this.instituteService.compareInstitute(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ report }) => {
      this.report = report;
      if (report) {
        this.updateForm(report);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('examManagerApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const report = this.reportFormService.getReport(this.editForm);
    if (report.id !== null) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
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

  protected updateForm(report: IReport): void {
    this.report = report;
    this.reportFormService.resetForm(this.editForm, report);

    this.professorDetailsSharedCollection = this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
      this.professorDetailsSharedCollection,
      report.professor,
    );
    this.examSessionsSharedCollection = this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(
      this.examSessionsSharedCollection,
      report.examSession,
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      report.department,
    );
    this.institutesSharedCollection = this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(
      this.institutesSharedCollection,
      report.institute,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professorDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfessorDetails[]>) => res.body ?? []))
      .pipe(
        map((professorDetails: IProfessorDetails[]) =>
          this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
            professorDetails,
            this.report?.professor,
          ),
        ),
      )
      .subscribe((professorDetails: IProfessorDetails[]) => (this.professorDetailsSharedCollection = professorDetails));

    this.examSessionService
      .query()
      .pipe(map((res: HttpResponse<IExamSession[]>) => res.body ?? []))
      .pipe(
        map((examSessions: IExamSession[]) =>
          this.examSessionService.addExamSessionToCollectionIfMissing<IExamSession>(examSessions, this.report?.examSession),
        ),
      )
      .subscribe((examSessions: IExamSession[]) => (this.examSessionsSharedCollection = examSessions));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.report?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.instituteService
      .query()
      .pipe(map((res: HttpResponse<IInstitute[]>) => res.body ?? []))
      .pipe(
        map((institutes: IInstitute[]) =>
          this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(institutes, this.report?.institute),
        ),
      )
      .subscribe((institutes: IInstitute[]) => (this.institutesSharedCollection = institutes));
  }
}
