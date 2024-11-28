import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDepartment, NewDepartment } from '../department.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartment for edit and NewDepartmentFormGroupInput for create.
 */
type DepartmentFormGroupInput = IDepartment | PartialWithRequiredKeyOf<NewDepartment>;

type DepartmentFormDefaults = Pick<NewDepartment, 'id' | 'examSessions'>;

type DepartmentFormGroupContent = {
  id: FormControl<IDepartment['id'] | NewDepartment['id']>;
  name: FormControl<IDepartment['name']>;
  email: FormControl<IDepartment['email']>;
  users: FormControl<IDepartment['users']>;
  examSessions: FormControl<IDepartment['examSessions']>;
  classroom: FormControl<IDepartment['classroom']>;
  major: FormControl<IDepartment['major']>;
  report: FormControl<IDepartment['report']>;
};

export type DepartmentFormGroup = FormGroup<DepartmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepartmentFormService {
  createDepartmentFormGroup(department: DepartmentFormGroupInput = { id: null }): DepartmentFormGroup {
    const departmentRawValue = {
      ...this.getFormDefaults(),
      ...department,
    };
    return new FormGroup<DepartmentFormGroupContent>({
      id: new FormControl(
        { value: departmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(departmentRawValue.name, {
        validators: [Validators.required],
      }),
      email: new FormControl(departmentRawValue.email),
      users: new FormControl(departmentRawValue.users),
      examSessions: new FormControl(departmentRawValue.examSessions ?? []),
      classroom: new FormControl(departmentRawValue.classroom),
      major: new FormControl(departmentRawValue.major),
      report: new FormControl(departmentRawValue.report),
    });
  }

  getDepartment(form: DepartmentFormGroup): IDepartment | NewDepartment {
    return form.getRawValue() as IDepartment | NewDepartment;
  }

  resetForm(form: DepartmentFormGroup, department: DepartmentFormGroupInput): void {
    const departmentRawValue = { ...this.getFormDefaults(), ...department };
    form.reset(
      {
        ...departmentRawValue,
        id: { value: departmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepartmentFormDefaults {
    return {
      id: null,
      examSessions: [],
    };
  }
}
