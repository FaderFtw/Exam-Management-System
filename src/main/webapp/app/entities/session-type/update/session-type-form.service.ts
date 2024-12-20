import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISessionType, NewSessionType } from '../session-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISessionType for edit and NewSessionTypeFormGroupInput for create.
 */
type SessionTypeFormGroupInput = ISessionType | PartialWithRequiredKeyOf<NewSessionType>;

type SessionTypeFormDefaults = Pick<NewSessionType, 'id'>;

type SessionTypeFormGroupContent = {
  id: FormControl<ISessionType['id'] | NewSessionType['id']>;
  name: FormControl<ISessionType['name']>;
};

export type SessionTypeFormGroup = FormGroup<SessionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SessionTypeFormService {
  createSessionTypeFormGroup(sessionType: SessionTypeFormGroupInput = { id: null }): SessionTypeFormGroup {
    const sessionTypeRawValue = {
      ...this.getFormDefaults(),
      ...sessionType,
    };
    return new FormGroup<SessionTypeFormGroupContent>({
      id: new FormControl(
        { value: sessionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(sessionTypeRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getSessionType(form: SessionTypeFormGroup): ISessionType | NewSessionType {
    return form.getRawValue() as ISessionType | NewSessionType;
  }

  resetForm(form: SessionTypeFormGroup, sessionType: SessionTypeFormGroupInput): void {
    const sessionTypeRawValue = { ...this.getFormDefaults(), ...sessionType };
    form.reset(
      {
        ...sessionTypeRawValue,
        id: { value: sessionTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SessionTypeFormDefaults {
    return {
      id: null,
    };
  }
}
