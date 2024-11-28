import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITeachingSession, NewTeachingSession } from '../teaching-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITeachingSession for edit and NewTeachingSessionFormGroupInput for create.
 */
type TeachingSessionFormGroupInput = ITeachingSession | PartialWithRequiredKeyOf<NewTeachingSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITeachingSession | NewTeachingSession> = Omit<T, 'startHour' | 'endHour'> & {
  startHour?: string | null;
  endHour?: string | null;
};

type TeachingSessionFormRawValue = FormValueOf<ITeachingSession>;

type NewTeachingSessionFormRawValue = FormValueOf<NewTeachingSession>;

type TeachingSessionFormDefaults = Pick<NewTeachingSession, 'id' | 'startHour' | 'endHour'>;

type TeachingSessionFormGroupContent = {
  id: FormControl<TeachingSessionFormRawValue['id'] | NewTeachingSession['id']>;
  name: FormControl<TeachingSessionFormRawValue['name']>;
  startHour: FormControl<TeachingSessionFormRawValue['startHour']>;
  endHour: FormControl<TeachingSessionFormRawValue['endHour']>;
  type: FormControl<TeachingSessionFormRawValue['type']>;
};

export type TeachingSessionFormGroup = FormGroup<TeachingSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TeachingSessionFormService {
  createTeachingSessionFormGroup(teachingSession: TeachingSessionFormGroupInput = { id: null }): TeachingSessionFormGroup {
    const teachingSessionRawValue = this.convertTeachingSessionToTeachingSessionRawValue({
      ...this.getFormDefaults(),
      ...teachingSession,
    });
    return new FormGroup<TeachingSessionFormGroupContent>({
      id: new FormControl(
        { value: teachingSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(teachingSessionRawValue.name, {
        validators: [Validators.required],
      }),
      startHour: new FormControl(teachingSessionRawValue.startHour, {
        validators: [Validators.required],
      }),
      endHour: new FormControl(teachingSessionRawValue.endHour, {
        validators: [Validators.required],
      }),
      type: new FormControl(teachingSessionRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getTeachingSession(form: TeachingSessionFormGroup): ITeachingSession | NewTeachingSession {
    return this.convertTeachingSessionRawValueToTeachingSession(
      form.getRawValue() as TeachingSessionFormRawValue | NewTeachingSessionFormRawValue,
    );
  }

  resetForm(form: TeachingSessionFormGroup, teachingSession: TeachingSessionFormGroupInput): void {
    const teachingSessionRawValue = this.convertTeachingSessionToTeachingSessionRawValue({ ...this.getFormDefaults(), ...teachingSession });
    form.reset(
      {
        ...teachingSessionRawValue,
        id: { value: teachingSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TeachingSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startHour: currentTime,
      endHour: currentTime,
    };
  }

  private convertTeachingSessionRawValueToTeachingSession(
    rawTeachingSession: TeachingSessionFormRawValue | NewTeachingSessionFormRawValue,
  ): ITeachingSession | NewTeachingSession {
    return {
      ...rawTeachingSession,
      startHour: dayjs(rawTeachingSession.startHour, DATE_TIME_FORMAT),
      endHour: dayjs(rawTeachingSession.endHour, DATE_TIME_FORMAT),
    };
  }

  private convertTeachingSessionToTeachingSessionRawValue(
    teachingSession: ITeachingSession | (Partial<NewTeachingSession> & TeachingSessionFormDefaults),
  ): TeachingSessionFormRawValue | PartialWithRequiredKeyOf<NewTeachingSessionFormRawValue> {
    return {
      ...teachingSession,
      startHour: teachingSession.startHour ? teachingSession.startHour.format(DATE_TIME_FORMAT) : undefined,
      endHour: teachingSession.endHour ? teachingSession.endHour.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
