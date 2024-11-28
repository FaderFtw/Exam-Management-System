import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IClassroom, NewClassroom } from '../classroom.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClassroom for edit and NewClassroomFormGroupInput for create.
 */
type ClassroomFormGroupInput = IClassroom | PartialWithRequiredKeyOf<NewClassroom>;

type ClassroomFormDefaults = Pick<NewClassroom, 'id'>;

type ClassroomFormGroupContent = {
  id: FormControl<IClassroom['id'] | NewClassroom['id']>;
  name: FormControl<IClassroom['name']>;
  capacity: FormControl<IClassroom['capacity']>;
  exam: FormControl<IClassroom['exam']>;
  teachingSession: FormControl<IClassroom['teachingSession']>;
};

export type ClassroomFormGroup = FormGroup<ClassroomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClassroomFormService {
  createClassroomFormGroup(classroom: ClassroomFormGroupInput = { id: null }): ClassroomFormGroup {
    const classroomRawValue = {
      ...this.getFormDefaults(),
      ...classroom,
    };
    return new FormGroup<ClassroomFormGroupContent>({
      id: new FormControl(
        { value: classroomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(classroomRawValue.name, {
        validators: [Validators.required],
      }),
      capacity: new FormControl(classroomRawValue.capacity, {
        validators: [Validators.required],
      }),
      exam: new FormControl(classroomRawValue.exam),
      teachingSession: new FormControl(classroomRawValue.teachingSession),
    });
  }

  getClassroom(form: ClassroomFormGroup): IClassroom | NewClassroom {
    return form.getRawValue() as IClassroom | NewClassroom;
  }

  resetForm(form: ClassroomFormGroup, classroom: ClassroomFormGroupInput): void {
    const classroomRawValue = { ...this.getFormDefaults(), ...classroom };
    form.reset(
      {
        ...classroomRawValue,
        id: { value: classroomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClassroomFormDefaults {
    return {
      id: null,
    };
  }
}
