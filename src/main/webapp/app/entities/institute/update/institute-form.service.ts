import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInstitute, NewInstitute } from '../institute.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInstitute for edit and NewInstituteFormGroupInput for create.
 */
type InstituteFormGroupInput = IInstitute | PartialWithRequiredKeyOf<NewInstitute>;

type InstituteFormDefaults = Pick<NewInstitute, 'id'>;

type InstituteFormGroupContent = {
  id: FormControl<IInstitute['id'] | NewInstitute['id']>;
  name: FormControl<IInstitute['name']>;
  location: FormControl<IInstitute['location']>;
  logo: FormControl<IInstitute['logo']>;
  logoContentType: FormControl<IInstitute['logoContentType']>;
  phone: FormControl<IInstitute['phone']>;
  email: FormControl<IInstitute['email']>;
  website: FormControl<IInstitute['website']>;
};

export type InstituteFormGroup = FormGroup<InstituteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InstituteFormService {
  createInstituteFormGroup(institute: InstituteFormGroupInput = { id: null }): InstituteFormGroup {
    const instituteRawValue = {
      ...this.getFormDefaults(),
      ...institute,
    };
    return new FormGroup<InstituteFormGroupContent>({
      id: new FormControl(
        { value: instituteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(instituteRawValue.name, {
        validators: [Validators.required],
      }),
      location: new FormControl(instituteRawValue.location, {
        validators: [Validators.required],
      }),
      logo: new FormControl(instituteRawValue.logo, {
        validators: [Validators.required],
      }),
      logoContentType: new FormControl(instituteRawValue.logoContentType),
      phone: new FormControl(instituteRawValue.phone),
      email: new FormControl(instituteRawValue.email),
      website: new FormControl(instituteRawValue.website),
    });
  }

  getInstitute(form: InstituteFormGroup): IInstitute | NewInstitute {
    return form.getRawValue() as IInstitute | NewInstitute;
  }

  resetForm(form: InstituteFormGroup, institute: InstituteFormGroupInput): void {
    const instituteRawValue = { ...this.getFormDefaults(), ...institute };
    form.reset(
      {
        ...instituteRawValue,
        id: { value: instituteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InstituteFormDefaults {
    return {
      id: null,
    };
  }
}
