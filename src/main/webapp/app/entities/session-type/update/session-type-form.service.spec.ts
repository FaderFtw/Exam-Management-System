import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../session-type.test-samples';

import { SessionTypeFormService } from './session-type-form.service';

describe('SessionType Form Service', () => {
  let service: SessionTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionTypeFormService);
  });

  describe('Service methods', () => {
    describe('createSessionTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSessionTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing ISessionType should create a new form with FormGroup', () => {
        const formGroup = service.createSessionTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getSessionType', () => {
      it('should return NewSessionType for default SessionType initial value', () => {
        const formGroup = service.createSessionTypeFormGroup(sampleWithNewData);

        const sessionType = service.getSessionType(formGroup) as any;

        expect(sessionType).toMatchObject(sampleWithNewData);
      });

      it('should return NewSessionType for empty SessionType initial value', () => {
        const formGroup = service.createSessionTypeFormGroup();

        const sessionType = service.getSessionType(formGroup) as any;

        expect(sessionType).toMatchObject({});
      });

      it('should return ISessionType', () => {
        const formGroup = service.createSessionTypeFormGroup(sampleWithRequiredData);

        const sessionType = service.getSessionType(formGroup) as any;

        expect(sessionType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISessionType should not enable id FormControl', () => {
        const formGroup = service.createSessionTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSessionType should disable id FormControl', () => {
        const formGroup = service.createSessionTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
