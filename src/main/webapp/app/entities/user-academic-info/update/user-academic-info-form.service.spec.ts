import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-academic-info.test-samples';

import { UserAcademicInfoFormService } from './user-academic-info-form.service';

describe('UserAcademicInfo Form Service', () => {
  let service: UserAcademicInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAcademicInfoFormService);
  });

  describe('Service methods', () => {
    describe('createUserAcademicInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserAcademicInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            phone: expect.any(Object),
            user: expect.any(Object),
            department: expect.any(Object),
            institute: expect.any(Object),
          }),
        );
      });

      it('passing IUserAcademicInfo should create a new form with FormGroup', () => {
        const formGroup = service.createUserAcademicInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            phone: expect.any(Object),
            user: expect.any(Object),
            department: expect.any(Object),
            institute: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserAcademicInfo', () => {
      it('should return NewUserAcademicInfo for default UserAcademicInfo initial value', () => {
        const formGroup = service.createUserAcademicInfoFormGroup(sampleWithNewData);

        const userAcademicInfo = service.getUserAcademicInfo(formGroup) as any;

        expect(userAcademicInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserAcademicInfo for empty UserAcademicInfo initial value', () => {
        const formGroup = service.createUserAcademicInfoFormGroup();

        const userAcademicInfo = service.getUserAcademicInfo(formGroup) as any;

        expect(userAcademicInfo).toMatchObject({});
      });

      it('should return IUserAcademicInfo', () => {
        const formGroup = service.createUserAcademicInfoFormGroup(sampleWithRequiredData);

        const userAcademicInfo = service.getUserAcademicInfo(formGroup) as any;

        expect(userAcademicInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserAcademicInfo should not enable id FormControl', () => {
        const formGroup = service.createUserAcademicInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserAcademicInfo should disable id FormControl', () => {
        const formGroup = service.createUserAcademicInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
