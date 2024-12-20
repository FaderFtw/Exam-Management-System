import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../professor-details.test-samples';

import { ProfessorDetailsFormService } from './professor-details-form.service';

describe('ProfessorDetails Form Service', () => {
  let service: ProfessorDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfessorDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createProfessorDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProfessorDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rank: expect.any(Object),
            user: expect.any(Object),
            supervisedExams: expect.any(Object),
          }),
        );
      });

      it('passing IProfessorDetails should create a new form with FormGroup', () => {
        const formGroup = service.createProfessorDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rank: expect.any(Object),
            user: expect.any(Object),
            supervisedExams: expect.any(Object),
          }),
        );
      });
    });

    describe('getProfessorDetails', () => {
      it('should return NewProfessorDetails for default ProfessorDetails initial value', () => {
        const formGroup = service.createProfessorDetailsFormGroup(sampleWithNewData);

        const professorDetails = service.getProfessorDetails(formGroup) as any;

        expect(professorDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewProfessorDetails for empty ProfessorDetails initial value', () => {
        const formGroup = service.createProfessorDetailsFormGroup();

        const professorDetails = service.getProfessorDetails(formGroup) as any;

        expect(professorDetails).toMatchObject({});
      });

      it('should return IProfessorDetails', () => {
        const formGroup = service.createProfessorDetailsFormGroup(sampleWithRequiredData);

        const professorDetails = service.getProfessorDetails(formGroup) as any;

        expect(professorDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProfessorDetails should not enable id FormControl', () => {
        const formGroup = service.createProfessorDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProfessorDetails should disable id FormControl', () => {
        const formGroup = service.createProfessorDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
