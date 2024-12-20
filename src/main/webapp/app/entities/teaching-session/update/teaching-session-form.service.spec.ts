import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../teaching-session.test-samples';

import { TeachingSessionFormService } from './teaching-session-form.service';

describe('TeachingSession Form Service', () => {
  let service: TeachingSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeachingSessionFormService);
  });

  describe('Service methods', () => {
    describe('createTeachingSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTeachingSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startHour: expect.any(Object),
            endHour: expect.any(Object),
            type: expect.any(Object),
            timetable: expect.any(Object),
            studentClass: expect.any(Object),
            classroom: expect.any(Object),
          }),
        );
      });

      it('passing ITeachingSession should create a new form with FormGroup', () => {
        const formGroup = service.createTeachingSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startHour: expect.any(Object),
            endHour: expect.any(Object),
            type: expect.any(Object),
            timetable: expect.any(Object),
            studentClass: expect.any(Object),
            classroom: expect.any(Object),
          }),
        );
      });
    });

    describe('getTeachingSession', () => {
      it('should return NewTeachingSession for default TeachingSession initial value', () => {
        const formGroup = service.createTeachingSessionFormGroup(sampleWithNewData);

        const teachingSession = service.getTeachingSession(formGroup) as any;

        expect(teachingSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewTeachingSession for empty TeachingSession initial value', () => {
        const formGroup = service.createTeachingSessionFormGroup();

        const teachingSession = service.getTeachingSession(formGroup) as any;

        expect(teachingSession).toMatchObject({});
      });

      it('should return ITeachingSession', () => {
        const formGroup = service.createTeachingSessionFormGroup(sampleWithRequiredData);

        const teachingSession = service.getTeachingSession(formGroup) as any;

        expect(teachingSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITeachingSession should not enable id FormControl', () => {
        const formGroup = service.createTeachingSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTeachingSession should disable id FormControl', () => {
        const formGroup = service.createTeachingSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
