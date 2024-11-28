import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type ExamFormDefaults = Pick<NewExam, 'id' | 'supervisors'>;

type ExamFormGroupContent = {
  id: FormControl<IExam['id'] | NewExam['id']>;
  name: FormControl<IExam['name']>;
  supervisors: FormControl<IExam['supervisors']>;
};

export type ExamFormGroup = FormGroup<ExamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExamFormService {
  createExamFormGroup(exam: ExamFormGroupInput = { id: null }): ExamFormGroup {
    const examRawValue = {
      ...this.getFormDefaults(),
      ...exam,
    };
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
      supervisors: new FormControl(examRawValue.supervisors ?? []),
    });
  }

  getExam(form: ExamFormGroup): IExam | NewExam {
    return form.getRawValue() as IExam | NewExam;
  }

  resetForm(form: ExamFormGroup, exam: ExamFormGroupInput): void {
    const examRawValue = { ...this.getFormDefaults(), ...exam };
    form.reset(
      {
        ...examRawValue,
        id: { value: examRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExamFormDefaults {
    return {
      id: null,
      supervisors: [],
    };
  }
}
