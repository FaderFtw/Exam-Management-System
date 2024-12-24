import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExam, NewExam } from '../exam.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExam for edit and NewExamFormGroupInput for create.
 */
type ExamFormGroupInput = IExam | PartialWithRequiredKeyOf<NewExam>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExam | NewExam> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type ExamFormRawValue = FormValueOf<IExam>;

type NewExamFormRawValue = FormValueOf<NewExam>;

type ExamFormDefaults = Pick<NewExam, 'id' | 'startTime' | 'endTime' | 'supervisors'>;

type ExamFormGroupContent = {
  id: FormControl<ExamFormRawValue['id'] | NewExam['id']>;
  name: FormControl<ExamFormRawValue['name']>;
  numberOfStudents: FormControl<ExamFormRawValue['numberOfStudents']>;
  startTime: FormControl<ExamFormRawValue['startTime']>;
  endTime: FormControl<ExamFormRawValue['endTime']>;
  classroom: FormControl<ExamFormRawValue['classroom']>;
  studentClass: FormControl<ExamFormRawValue['studentClass']>;
  session: FormControl<ExamFormRawValue['session']>;
  supervisors: FormControl<ExamFormRawValue['supervisors']>;
};

export type ExamFormGroup = FormGroup<ExamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExamFormService {
  createExamFormGroup(exam: ExamFormGroupInput = { id: null }): ExamFormGroup {
    const examRawValue = this.convertExamToExamRawValue({
      ...this.getFormDefaults(),
      ...exam,
    });
    return new FormGroup<ExamFormGroupContent>({
      id: new FormControl(
        { value: examRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(examRawValue.name, {
        validators: [Validators.required],
      }),
      numberOfStudents: new FormControl(examRawValue.numberOfStudents, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(examRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(examRawValue.endTime, {
        validators: [Validators.required],
      }),
      classroom: new FormControl(examRawValue.classroom),
      studentClass: new FormControl(examRawValue.studentClass),
      session: new FormControl(examRawValue.session),
      supervisors: new FormControl(examRawValue.supervisors ?? []),
    });
  }

  getExam(form: ExamFormGroup): IExam | NewExam {
    return this.convertExamRawValueToExam(form.getRawValue() as ExamFormRawValue | NewExamFormRawValue);
  }

  resetForm(form: ExamFormGroup, exam: ExamFormGroupInput): void {
    const examRawValue = this.convertExamToExamRawValue({ ...this.getFormDefaults(), ...exam });
    form.reset(
      {
        ...examRawValue,
        id: { value: examRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExamFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
      supervisors: [],
    };
  }

  private convertExamRawValueToExam(rawExam: ExamFormRawValue | NewExamFormRawValue): IExam | NewExam {
    return {
      ...rawExam,
      startTime: dayjs(rawExam.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawExam.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertExamToExamRawValue(
    exam: IExam | (Partial<NewExam> & ExamFormDefaults),
  ): ExamFormRawValue | PartialWithRequiredKeyOf<NewExamFormRawValue> {
    return {
      ...exam,
      startTime: exam.startTime ? exam.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: exam.endTime ? exam.endTime.format(DATE_TIME_FORMAT) : undefined,
      supervisors: exam.supervisors ?? [],
    };
  }
}
