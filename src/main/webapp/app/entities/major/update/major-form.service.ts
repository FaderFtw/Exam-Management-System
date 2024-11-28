import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMajor, NewMajor } from '../major.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMajor for edit and NewMajorFormGroupInput for create.
 */
type MajorFormGroupInput = IMajor | PartialWithRequiredKeyOf<NewMajor>;

type MajorFormDefaults = Pick<NewMajor, 'id'>;

type MajorFormGroupContent = {
  id: FormControl<IMajor['id'] | NewMajor['id']>;
  name: FormControl<IMajor['name']>;
  studentClass: FormControl<IMajor['studentClass']>;
};

export type MajorFormGroup = FormGroup<MajorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MajorFormService {
  createMajorFormGroup(major: MajorFormGroupInput = { id: null }): MajorFormGroup {
    const majorRawValue = {
      ...this.getFormDefaults(),
      ...major,
    };
    return new FormGroup<MajorFormGroupContent>({
      id: new FormControl(
        { value: majorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(majorRawValue.name, {
        validators: [Validators.required],
      }),
      studentClass: new FormControl(majorRawValue.studentClass),
    });
  }

  getMajor(form: MajorFormGroup): IMajor | NewMajor {
    return form.getRawValue() as IMajor | NewMajor;
  }

  resetForm(form: MajorFormGroup, major: MajorFormGroupInput): void {
    const majorRawValue = { ...this.getFormDefaults(), ...major };
    form.reset(
      {
        ...majorRawValue,
        id: { value: majorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MajorFormDefaults {
    return {
      id: null,
    };
  }
}
