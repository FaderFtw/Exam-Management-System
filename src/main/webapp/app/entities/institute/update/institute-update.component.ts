import { Component, ElementRef, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { InstituteService } from '../service/institute.service';
import { IInstitute } from '../institute.model';
import { InstituteFormGroup, InstituteFormService } from './institute-form.service';

@Component({
  standalone: true,
  selector: 'jhi-institute-update',
  templateUrl: './institute-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InstituteUpdateComponent implements OnInit {
  isSaving = false;
  institute: IInstitute | null = null;

  departmentsSharedCollection: IDepartment[] = [];
  reportsSharedCollection: IReport[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected instituteService = inject(InstituteService);
  protected instituteFormService = inject(InstituteFormService);
  protected departmentService = inject(DepartmentService);
  protected reportService = inject(ReportService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InstituteFormGroup = this.instituteFormService.createInstituteFormGroup();

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareReport = (o1: IReport | null, o2: IReport | null): boolean => this.reportService.compareReport(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ institute }) => {
      this.institute = institute;
      if (institute) {
        this.updateForm(institute);
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const institute = this.instituteFormService.getInstitute(this.editForm);
    if (institute.id !== null) {
      this.subscribeToSaveResponse(this.instituteService.update(institute));
    } else {
      this.subscribeToSaveResponse(this.instituteService.create(institute));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstitute>>): void {
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

  protected updateForm(institute: IInstitute): void {
    this.institute = institute;
    this.instituteFormService.resetForm(this.editForm, institute);

    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      institute.department,
    );
    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing<IReport>(
      this.reportsSharedCollection,
      institute.report,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.institute?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing<IReport>(reports, this.institute?.report)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }
}
