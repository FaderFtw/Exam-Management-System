import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISessionType } from '../session-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../session-type.test-samples';

import { SessionTypeService } from './session-type.service';

const requireRestSample: ISessionType = {
  ...sampleWithRequiredData,
};

describe('SessionType Service', () => {
  let service: SessionTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ISessionType | ISessionType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SessionTypeService);
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

    it('should create a SessionType', () => {
      const sessionType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sessionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SessionType', () => {
      const sessionType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sessionType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SessionType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SessionType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SessionType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSessionTypeToCollectionIfMissing', () => {
      it('should add a SessionType to an empty array', () => {
        const sessionType: ISessionType = sampleWithRequiredData;
        expectedResult = service.addSessionTypeToCollectionIfMissing([], sessionType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionType);
      });

      it('should not add a SessionType to an array that contains it', () => {
        const sessionType: ISessionType = sampleWithRequiredData;
        const sessionTypeCollection: ISessionType[] = [
          {
            ...sessionType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSessionTypeToCollectionIfMissing(sessionTypeCollection, sessionType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SessionType to an array that doesn't contain it", () => {
        const sessionType: ISessionType = sampleWithRequiredData;
        const sessionTypeCollection: ISessionType[] = [sampleWithPartialData];
        expectedResult = service.addSessionTypeToCollectionIfMissing(sessionTypeCollection, sessionType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionType);
      });

      it('should add only unique SessionType to an array', () => {
        const sessionTypeArray: ISessionType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sessionTypeCollection: ISessionType[] = [sampleWithRequiredData];
        expectedResult = service.addSessionTypeToCollectionIfMissing(sessionTypeCollection, ...sessionTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sessionType: ISessionType = sampleWithRequiredData;
        const sessionType2: ISessionType = sampleWithPartialData;
        expectedResult = service.addSessionTypeToCollectionIfMissing([], sessionType, sessionType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sessionType);
        expect(expectedResult).toContain(sessionType2);
      });

      it('should accept null and undefined values', () => {
        const sessionType: ISessionType = sampleWithRequiredData;
        expectedResult = service.addSessionTypeToCollectionIfMissing([], null, sessionType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sessionType);
      });

      it('should return initial array if no SessionType is added', () => {
        const sessionTypeCollection: ISessionType[] = [sampleWithRequiredData];
        expectedResult = service.addSessionTypeToCollectionIfMissing(sessionTypeCollection, undefined, null);
        expect(expectedResult).toEqual(sessionTypeCollection);
      });
    });

    describe('compareSessionType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSessionType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSessionType(entity1, entity2);
        const compareResult2 = service.compareSessionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSessionType(entity1, entity2);
        const compareResult2 = service.compareSessionType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSessionType(entity1, entity2);
        const compareResult2 = service.compareSessionType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
