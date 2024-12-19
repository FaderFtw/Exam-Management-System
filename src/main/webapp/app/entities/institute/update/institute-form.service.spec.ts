import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../institute.test-samples';

import { InstituteFormService } from './institute-form.service';

describe('Institute Form Service', () => {
  let service: InstituteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstituteFormService);
  });

  describe('Service methods', () => {
    describe('createInstituteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInstituteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            location: expect.any(Object),
            logo: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            website: expect.any(Object),
          }),
        );
      });

      it('passing IInstitute should create a new form with FormGroup', () => {
        const formGroup = service.createInstituteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            location: expect.any(Object),
            logo: expect.any(Object),
            phone: expect.any(Object),
            email: expect.any(Object),
            website: expect.any(Object),
          }),
        );
      });
    });

    describe('getInstitute', () => {
      it('should return NewInstitute for default Institute initial value', () => {
        const formGroup = service.createInstituteFormGroup(sampleWithNewData);

        const institute = service.getInstitute(formGroup) as any;

        expect(institute).toMatchObject(sampleWithNewData);
      });

      it('should return NewInstitute for empty Institute initial value', () => {
        const formGroup = service.createInstituteFormGroup();

        const institute = service.getInstitute(formGroup) as any;

        expect(institute).toMatchObject({});
      });

      it('should return IInstitute', () => {
        const formGroup = service.createInstituteFormGroup(sampleWithRequiredData);

        const institute = service.getInstitute(formGroup) as any;

        expect(institute).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInstitute should not enable id FormControl', () => {
        const formGroup = service.createInstituteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInstitute should disable id FormControl', () => {
        const formGroup = service.createInstituteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
