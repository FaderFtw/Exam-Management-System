import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../student-class.test-samples';

import { StudentClassFormService } from './student-class-form.service';

describe('StudentClass Form Service', () => {
  let service: StudentClassFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentClassFormService);
  });

  describe('Service methods', () => {
    describe('createStudentClassFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStudentClassFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            studentCount: expect.any(Object),
            exam: expect.any(Object),
            teachingSession: expect.any(Object),
          }),
        );
      });

      it('passing IStudentClass should create a new form with FormGroup', () => {
        const formGroup = service.createStudentClassFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            studentCount: expect.any(Object),
            exam: expect.any(Object),
            teachingSession: expect.any(Object),
          }),
        );
      });
    });

    describe('getStudentClass', () => {
      it('should return NewStudentClass for default StudentClass initial value', () => {
        const formGroup = service.createStudentClassFormGroup(sampleWithNewData);

        const studentClass = service.getStudentClass(formGroup) as any;

        expect(studentClass).toMatchObject(sampleWithNewData);
      });

      it('should return NewStudentClass for empty StudentClass initial value', () => {
        const formGroup = service.createStudentClassFormGroup();

        const studentClass = service.getStudentClass(formGroup) as any;

        expect(studentClass).toMatchObject({});
      });

      it('should return IStudentClass', () => {
        const formGroup = service.createStudentClassFormGroup(sampleWithRequiredData);

        const studentClass = service.getStudentClass(formGroup) as any;

        expect(studentClass).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStudentClass should not enable id FormControl', () => {
        const formGroup = service.createStudentClassFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStudentClass should disable id FormControl', () => {
        const formGroup = service.createStudentClassFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
