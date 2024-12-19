import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../exam-session.test-samples';

import { ExamSessionFormService } from './exam-session-form.service';

describe('ExamSession Form Service', () => {
  let service: ExamSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExamSessionFormService);
  });

  describe('Service methods', () => {
    describe('createExamSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExamSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sessionCode: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            allowParallelStudies: expect.any(Object),
            allowOwnClassSupervision: expect.any(Object),
            allowCombineClasses: expect.any(Object),
            sessionType: expect.any(Object),
            departments: expect.any(Object),
          }),
        );
      });

      it('passing IExamSession should create a new form with FormGroup', () => {
        const formGroup = service.createExamSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            sessionCode: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            allowParallelStudies: expect.any(Object),
            allowOwnClassSupervision: expect.any(Object),
            allowCombineClasses: expect.any(Object),
            sessionType: expect.any(Object),
            departments: expect.any(Object),
          }),
        );
      });
    });

    describe('getExamSession', () => {
      it('should return NewExamSession for default ExamSession initial value', () => {
        const formGroup = service.createExamSessionFormGroup(sampleWithNewData);

        const examSession = service.getExamSession(formGroup) as any;

        expect(examSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewExamSession for empty ExamSession initial value', () => {
        const formGroup = service.createExamSessionFormGroup();

        const examSession = service.getExamSession(formGroup) as any;

        expect(examSession).toMatchObject({});
      });

      it('should return IExamSession', () => {
        const formGroup = service.createExamSessionFormGroup(sampleWithRequiredData);

        const examSession = service.getExamSession(formGroup) as any;

        expect(examSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExamSession should not enable id FormControl', () => {
        const formGroup = service.createExamSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExamSession should disable id FormControl', () => {
        const formGroup = service.createExamSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
