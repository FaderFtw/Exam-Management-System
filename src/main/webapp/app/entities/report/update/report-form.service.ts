import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IReport, NewReport } from '../report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReport for edit and NewReportFormGroupInput for create.
 */
type ReportFormGroupInput = IReport | PartialWithRequiredKeyOf<NewReport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IReport | NewReport> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type ReportFormRawValue = FormValueOf<IReport>;

type NewReportFormRawValue = FormValueOf<NewReport>;

type ReportFormDefaults = Pick<NewReport, 'id' | 'createdDate'>;

type ReportFormGroupContent = {
  id: FormControl<ReportFormRawValue['id'] | NewReport['id']>;
  name: FormControl<ReportFormRawValue['name']>;
  createdDate: FormControl<ReportFormRawValue['createdDate']>;
  content: FormControl<ReportFormRawValue['content']>;
};

export type ReportFormGroup = FormGroup<ReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportFormService {
  createReportFormGroup(report: ReportFormGroupInput = { id: null }): ReportFormGroup {
    const reportRawValue = this.convertReportToReportRawValue({
      ...this.getFormDefaults(),
      ...report,
    });
    return new FormGroup<ReportFormGroupContent>({
      id: new FormControl(
        { value: reportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportRawValue.name, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(reportRawValue.createdDate, {
        validators: [Validators.required],
      }),
      content: new FormControl(reportRawValue.content),
    });
  }

  getReport(form: ReportFormGroup): IReport | NewReport {
    return this.convertReportRawValueToReport(form.getRawValue() as ReportFormRawValue | NewReportFormRawValue);
  }

  resetForm(form: ReportFormGroup, report: ReportFormGroupInput): void {
    const reportRawValue = this.convertReportToReportRawValue({ ...this.getFormDefaults(), ...report });
    form.reset(
      {
        ...reportRawValue,
        id: { value: reportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertReportRawValueToReport(rawReport: ReportFormRawValue | NewReportFormRawValue): IReport | NewReport {
    return {
      ...rawReport,
      createdDate: dayjs(rawReport.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertReportToReportRawValue(
    report: IReport | (Partial<NewReport> & ReportFormDefaults),
  ): ReportFormRawValue | PartialWithRequiredKeyOf<NewReportFormRawValue> {
    return {
      ...report,
      createdDate: report.createdDate ? report.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
