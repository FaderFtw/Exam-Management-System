import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../classroom.test-samples';

import { ClassroomFormService } from './classroom-form.service';

describe('Classroom Form Service', () => {
  let service: ClassroomFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassroomFormService);
  });

  describe('Service methods', () => {
    describe('createClassroomFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClassroomFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            capacity: expect.any(Object),
            department: expect.any(Object),
          }),
        );
      });

      it('passing IClassroom should create a new form with FormGroup', () => {
        const formGroup = service.createClassroomFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            capacity: expect.any(Object),
            department: expect.any(Object),
          }),
        );
      });
    });

    describe('getClassroom', () => {
      it('should return NewClassroom for default Classroom initial value', () => {
        const formGroup = service.createClassroomFormGroup(sampleWithNewData);

        const classroom = service.getClassroom(formGroup) as any;

        expect(classroom).toMatchObject(sampleWithNewData);
      });

      it('should return NewClassroom for empty Classroom initial value', () => {
        const formGroup = service.createClassroomFormGroup();

        const classroom = service.getClassroom(formGroup) as any;

        expect(classroom).toMatchObject({});
      });

      it('should return IClassroom', () => {
        const formGroup = service.createClassroomFormGroup(sampleWithRequiredData);

        const classroom = service.getClassroom(formGroup) as any;

        expect(classroom).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClassroom should not enable id FormControl', () => {
        const formGroup = service.createClassroomFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClassroom should disable id FormControl', () => {
        const formGroup = service.createClassroomFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
