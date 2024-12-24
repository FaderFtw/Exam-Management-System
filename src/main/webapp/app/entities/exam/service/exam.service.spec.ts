import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExam } from '../exam.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../exam.test-samples';

import { ExamService, RestExam } from './exam.service';

const requireRestSample: RestExam = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('Exam Service', () => {
  let service: ExamService;
  let httpMock: HttpTestingController;
  let expectedResult: IExam | IExam[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExamService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Exam', () => {
      const exam = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(exam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Exam', () => {
      const exam = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(exam).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Exam', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Exam', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Exam', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExamToCollectionIfMissing', () => {
      it('should add a Exam to an empty array', () => {
        const exam: IExam = sampleWithRequiredData;
        expectedResult = service.addExamToCollectionIfMissing([], exam);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exam);
      });

      it('should not add a Exam to an array that contains it', () => {
        const exam: IExam = sampleWithRequiredData;
        const examCollection: IExam[] = [
          {
            ...exam,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExamToCollectionIfMissing(examCollection, exam);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Exam to an array that doesn't contain it", () => {
        const exam: IExam = sampleWithRequiredData;
        const examCollection: IExam[] = [sampleWithPartialData];
        expectedResult = service.addExamToCollectionIfMissing(examCollection, exam);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exam);
      });

      it('should add only unique Exam to an array', () => {
        const examArray: IExam[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const examCollection: IExam[] = [sampleWithRequiredData];
        expectedResult = service.addExamToCollectionIfMissing(examCollection, ...examArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exam: IExam = sampleWithRequiredData;
        const exam2: IExam = sampleWithPartialData;
        expectedResult = service.addExamToCollectionIfMissing([], exam, exam2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exam);
        expect(expectedResult).toContain(exam2);
      });

      it('should accept null and undefined values', () => {
        const exam: IExam = sampleWithRequiredData;
        expectedResult = service.addExamToCollectionIfMissing([], null, exam, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exam);
      });

      it('should return initial array if no Exam is added', () => {
        const examCollection: IExam[] = [sampleWithRequiredData];
        expectedResult = service.addExamToCollectionIfMissing(examCollection, undefined, null);
        expect(expectedResult).toEqual(examCollection);
      });
    });

    describe('compareExam', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExam(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExam(entity1, entity2);
        const compareResult2 = service.compareExam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExam(entity1, entity2);
        const compareResult2 = service.compareExam(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExam(entity1, entity2);
        const compareResult2 = service.compareExam(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
