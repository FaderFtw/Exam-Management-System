import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProfessorDetails, NewProfessorDetails } from '../professor-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfessorDetails for edit and NewProfessorDetailsFormGroupInput for create.
 */
type ProfessorDetailsFormGroupInput = IProfessorDetails | PartialWithRequiredKeyOf<NewProfessorDetails>;

type ProfessorDetailsFormDefaults = Pick<NewProfessorDetails, 'id' | 'supervisedExams'>;

type ProfessorDetailsFormGroupContent = {
  id: FormControl<IProfessorDetails['id'] | NewProfessorDetails['id']>;
  rank: FormControl<IProfessorDetails['rank']>;
  user: FormControl<IProfessorDetails['user']>;
  supervisedExams: FormControl<IProfessorDetails['supervisedExams']>;
};

export type ProfessorDetailsFormGroup = FormGroup<ProfessorDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfessorDetailsFormService {
  createProfessorDetailsFormGroup(professorDetails: ProfessorDetailsFormGroupInput = { id: null }): ProfessorDetailsFormGroup {
    const professorDetailsRawValue = {
      ...this.getFormDefaults(),
      ...professorDetails,
    };
    return new FormGroup<ProfessorDetailsFormGroupContent>({
      id: new FormControl(
        { value: professorDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      rank: new FormControl(professorDetailsRawValue.rank, {
        validators: [Validators.required],
      }),
      user: new FormControl(professorDetailsRawValue.user),
      supervisedExams: new FormControl(professorDetailsRawValue.supervisedExams ?? []),
    });
  }

  getProfessorDetails(form: ProfessorDetailsFormGroup): IProfessorDetails | NewProfessorDetails {
    return form.getRawValue() as IProfessorDetails | NewProfessorDetails;
  }

  resetForm(form: ProfessorDetailsFormGroup, professorDetails: ProfessorDetailsFormGroupInput): void {
    const professorDetailsRawValue = { ...this.getFormDefaults(), ...professorDetails };
    form.reset(
      {
        ...professorDetailsRawValue,
        id: { value: professorDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfessorDetailsFormDefaults {
    return {
      id: null,
      supervisedExams: [],
    };
  }
}
