import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../major.test-samples';

import { MajorFormService } from './major-form.service';

describe('Major Form Service', () => {
  let service: MajorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MajorFormService);
  });

  describe('Service methods', () => {
    describe('createMajorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMajorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            studentClass: expect.any(Object),
          }),
        );
      });

      it('passing IMajor should create a new form with FormGroup', () => {
        const formGroup = service.createMajorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            studentClass: expect.any(Object),
          }),
        );
      });
    });

    describe('getMajor', () => {
      it('should return NewMajor for default Major initial value', () => {
        const formGroup = service.createMajorFormGroup(sampleWithNewData);

        const major = service.getMajor(formGroup) as any;

        expect(major).toMatchObject(sampleWithNewData);
      });

      it('should return NewMajor for empty Major initial value', () => {
        const formGroup = service.createMajorFormGroup();

        const major = service.getMajor(formGroup) as any;

        expect(major).toMatchObject({});
      });

      it('should return IMajor', () => {
        const formGroup = service.createMajorFormGroup(sampleWithRequiredData);

        const major = service.getMajor(formGroup) as any;

        expect(major).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMajor should not enable id FormControl', () => {
        const formGroup = service.createMajorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMajor should disable id FormControl', () => {
        const formGroup = service.createMajorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
