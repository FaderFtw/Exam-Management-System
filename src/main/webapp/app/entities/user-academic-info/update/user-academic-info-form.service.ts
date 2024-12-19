import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserAcademicInfo, NewUserAcademicInfo } from '../user-academic-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserAcademicInfo for edit and NewUserAcademicInfoFormGroupInput for create.
 */
type UserAcademicInfoFormGroupInput = IUserAcademicInfo | PartialWithRequiredKeyOf<NewUserAcademicInfo>;

type UserAcademicInfoFormDefaults = Pick<NewUserAcademicInfo, 'id'>;

type UserAcademicInfoFormGroupContent = {
  id: FormControl<IUserAcademicInfo['id'] | NewUserAcademicInfo['id']>;
  user: FormControl<IUserAcademicInfo['user']>;
  department: FormControl<IUserAcademicInfo['department']>;
  institute: FormControl<IUserAcademicInfo['institute']>;
};

export type UserAcademicInfoFormGroup = FormGroup<UserAcademicInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserAcademicInfoFormService {
  createUserAcademicInfoFormGroup(userAcademicInfo: UserAcademicInfoFormGroupInput = { id: null }): UserAcademicInfoFormGroup {
    const userAcademicInfoRawValue = {
      ...this.getFormDefaults(),
      ...userAcademicInfo,
    };
    return new FormGroup<UserAcademicInfoFormGroupContent>({
      id: new FormControl(
        { value: userAcademicInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      user: new FormControl(userAcademicInfoRawValue.user, {
        validators: [Validators.required],
      }),
      department: new FormControl(userAcademicInfoRawValue.department),
      institute: new FormControl(userAcademicInfoRawValue.institute),
    });
  }

  getUserAcademicInfo(form: UserAcademicInfoFormGroup): IUserAcademicInfo | NewUserAcademicInfo {
    return form.getRawValue() as IUserAcademicInfo | NewUserAcademicInfo;
  }

  resetForm(form: UserAcademicInfoFormGroup, userAcademicInfo: UserAcademicInfoFormGroupInput): void {
    const userAcademicInfoRawValue = { ...this.getFormDefaults(), ...userAcademicInfo };
    form.reset(
      {
        ...userAcademicInfoRawValue,
        id: { value: userAcademicInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserAcademicInfoFormDefaults {
    return {
      id: null,
    };
  }
}
