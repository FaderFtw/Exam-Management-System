import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../exam.test-samples';

import { ExamFormService } from './exam-form.service';

describe('Exam Form Service', () => {
  let service: ExamFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExamFormService);
  });

  describe('Service methods', () => {
    describe('createExamFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExamFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            numberOfStudents: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            classroom: expect.any(Object),
            studentClass: expect.any(Object),
            session: expect.any(Object),
            supervisors: expect.any(Object),
          }),
        );
      });

      it('passing IExam should create a new form with FormGroup', () => {
        const formGroup = service.createExamFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            numberOfStudents: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            classroom: expect.any(Object),
            studentClass: expect.any(Object),
            session: expect.any(Object),
            supervisors: expect.any(Object),
          }),
        );
      });
    });

    describe('getExam', () => {
      it('should return NewExam for default Exam initial value', () => {
        const formGroup = service.createExamFormGroup(sampleWithNewData);

        const exam = service.getExam(formGroup) as any;

        expect(exam).toMatchObject(sampleWithNewData);
      });

      it('should return NewExam for empty Exam initial value', () => {
        const formGroup = service.createExamFormGroup();

        const exam = service.getExam(formGroup) as any;

        expect(exam).toMatchObject({});
      });

      it('should return IExam', () => {
        const formGroup = service.createExamFormGroup(sampleWithRequiredData);

        const exam = service.getExam(formGroup) as any;

        expect(exam).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExam should not enable id FormControl', () => {
        const formGroup = service.createExamFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExam should disable id FormControl', () => {
        const formGroup = service.createExamFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
