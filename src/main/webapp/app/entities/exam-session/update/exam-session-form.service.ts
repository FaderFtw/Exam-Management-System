import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IExamSession, NewExamSession } from '../exam-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExamSession for edit and NewExamSessionFormGroupInput for create.
 */
type ExamSessionFormGroupInput = IExamSession | PartialWithRequiredKeyOf<NewExamSession>;

type ExamSessionFormDefaults = Pick<
  NewExamSession,
  'id' | 'allowParallelStudies' | 'allowOwnClassSupervision' | 'allowCombineClasses' | 'departments'
>;

type ExamSessionFormGroupContent = {
  id: FormControl<IExamSession['id'] | NewExamSession['id']>;
  name: FormControl<IExamSession['name']>;
  sessionCode: FormControl<IExamSession['sessionCode']>;
  startDate: FormControl<IExamSession['startDate']>;
  endDate: FormControl<IExamSession['endDate']>;
  allowParallelStudies: FormControl<IExamSession['allowParallelStudies']>;
  allowOwnClassSupervision: FormControl<IExamSession['allowOwnClassSupervision']>;
  allowCombineClasses: FormControl<IExamSession['allowCombineClasses']>;
  sessionType: FormControl<IExamSession['sessionType']>;
  departments: FormControl<IExamSession['departments']>;
};

export type ExamSessionFormGroup = FormGroup<ExamSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExamSessionFormService {
  createExamSessionFormGroup(examSession: ExamSessionFormGroupInput = { id: null }): ExamSessionFormGroup {
    const examSessionRawValue = {
      ...this.getFormDefaults(),
      ...examSession,
    };
    return new FormGroup<ExamSessionFormGroupContent>({
      id: new FormControl(
        { value: examSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(examSessionRawValue.name, {
        validators: [Validators.required],
      }),
      sessionCode: new FormControl(examSessionRawValue.sessionCode, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(examSessionRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(examSessionRawValue.endDate, {
        validators: [Validators.required],
      }),
      allowParallelStudies: new FormControl(examSessionRawValue.allowParallelStudies, {
        validators: [Validators.required],
      }),
      allowOwnClassSupervision: new FormControl(examSessionRawValue.allowOwnClassSupervision, {
        validators: [Validators.required],
      }),
      allowCombineClasses: new FormControl(examSessionRawValue.allowCombineClasses, {
        validators: [Validators.required],
      }),
      sessionType: new FormControl(examSessionRawValue.sessionType),
      departments: new FormControl(examSessionRawValue.departments ?? []),
    });
  }

  getExamSession(form: ExamSessionFormGroup): IExamSession | NewExamSession {
    return form.getRawValue() as IExamSession | NewExamSession;
  }

  resetForm(form: ExamSessionFormGroup, examSession: ExamSessionFormGroupInput): void {
    const examSessionRawValue = { ...this.getFormDefaults(), ...examSession };
    form.reset(
      {
        ...examSessionRawValue,
        id: { value: examSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExamSessionFormDefaults {
    return {
      id: null,
      allowParallelStudies: false,
      allowOwnClassSupervision: false,
      allowCombineClasses: false,
      departments: [],
    };
  }
}
