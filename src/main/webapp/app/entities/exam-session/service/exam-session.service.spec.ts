import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExamSession } from '../exam-session.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../exam-session.test-samples';

import { ExamSessionService, RestExamSession } from './exam-session.service';

const requireRestSample: RestExamSession = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('ExamSession Service', () => {
  let service: ExamSessionService;
  let httpMock: HttpTestingController;
  let expectedResult: IExamSession | IExamSession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExamSessionService);
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

    it('should create a ExamSession', () => {
      const examSession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(examSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExamSession', () => {
      const examSession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(examSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExamSession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExamSession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExamSession', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExamSessionToCollectionIfMissing', () => {
      it('should add a ExamSession to an empty array', () => {
        const examSession: IExamSession = sampleWithRequiredData;
        expectedResult = service.addExamSessionToCollectionIfMissing([], examSession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(examSession);
      });

      it('should not add a ExamSession to an array that contains it', () => {
        const examSession: IExamSession = sampleWithRequiredData;
        const examSessionCollection: IExamSession[] = [
          {
            ...examSession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExamSessionToCollectionIfMissing(examSessionCollection, examSession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExamSession to an array that doesn't contain it", () => {
        const examSession: IExamSession = sampleWithRequiredData;
        const examSessionCollection: IExamSession[] = [sampleWithPartialData];
        expectedResult = service.addExamSessionToCollectionIfMissing(examSessionCollection, examSession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(examSession);
      });

      it('should add only unique ExamSession to an array', () => {
        const examSessionArray: IExamSession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const examSessionCollection: IExamSession[] = [sampleWithRequiredData];
        expectedResult = service.addExamSessionToCollectionIfMissing(examSessionCollection, ...examSessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const examSession: IExamSession = sampleWithRequiredData;
        const examSession2: IExamSession = sampleWithPartialData;
        expectedResult = service.addExamSessionToCollectionIfMissing([], examSession, examSession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(examSession);
        expect(expectedResult).toContain(examSession2);
      });

      it('should accept null and undefined values', () => {
        const examSession: IExamSession = sampleWithRequiredData;
        expectedResult = service.addExamSessionToCollectionIfMissing([], null, examSession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(examSession);
      });

      it('should return initial array if no ExamSession is added', () => {
        const examSessionCollection: IExamSession[] = [sampleWithRequiredData];
        expectedResult = service.addExamSessionToCollectionIfMissing(examSessionCollection, undefined, null);
        expect(expectedResult).toEqual(examSessionCollection);
      });
    });

    describe('compareExamSession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExamSession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExamSession(entity1, entity2);
        const compareResult2 = service.compareExamSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExamSession(entity1, entity2);
        const compareResult2 = service.compareExamSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExamSession(entity1, entity2);
        const compareResult2 = service.compareExamSession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
