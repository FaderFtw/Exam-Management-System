import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITimetable } from '../timetable.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../timetable.test-samples';

import { RestTimetable, TimetableService } from './timetable.service';

const requireRestSample: RestTimetable = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Timetable Service', () => {
  let service: TimetableService;
  let httpMock: HttpTestingController;
  let expectedResult: ITimetable | ITimetable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TimetableService);
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

    it('should create a Timetable', () => {
      const timetable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(timetable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Timetable', () => {
      const timetable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(timetable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Timetable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Timetable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Timetable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTimetableToCollectionIfMissing', () => {
      it('should add a Timetable to an empty array', () => {
        const timetable: ITimetable = sampleWithRequiredData;
        expectedResult = service.addTimetableToCollectionIfMissing([], timetable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(timetable);
      });

      it('should not add a Timetable to an array that contains it', () => {
        const timetable: ITimetable = sampleWithRequiredData;
        const timetableCollection: ITimetable[] = [
          {
            ...timetable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTimetableToCollectionIfMissing(timetableCollection, timetable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Timetable to an array that doesn't contain it", () => {
        const timetable: ITimetable = sampleWithRequiredData;
        const timetableCollection: ITimetable[] = [sampleWithPartialData];
        expectedResult = service.addTimetableToCollectionIfMissing(timetableCollection, timetable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(timetable);
      });

      it('should add only unique Timetable to an array', () => {
        const timetableArray: ITimetable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const timetableCollection: ITimetable[] = [sampleWithRequiredData];
        expectedResult = service.addTimetableToCollectionIfMissing(timetableCollection, ...timetableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const timetable: ITimetable = sampleWithRequiredData;
        const timetable2: ITimetable = sampleWithPartialData;
        expectedResult = service.addTimetableToCollectionIfMissing([], timetable, timetable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(timetable);
        expect(expectedResult).toContain(timetable2);
      });

      it('should accept null and undefined values', () => {
        const timetable: ITimetable = sampleWithRequiredData;
        expectedResult = service.addTimetableToCollectionIfMissing([], null, timetable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(timetable);
      });

      it('should return initial array if no Timetable is added', () => {
        const timetableCollection: ITimetable[] = [sampleWithRequiredData];
        expectedResult = service.addTimetableToCollectionIfMissing(timetableCollection, undefined, null);
        expect(expectedResult).toEqual(timetableCollection);
      });
    });

    describe('compareTimetable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTimetable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTimetable(entity1, entity2);
        const compareResult2 = service.compareTimetable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTimetable(entity1, entity2);
        const compareResult2 = service.compareTimetable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTimetable(entity1, entity2);
        const compareResult2 = service.compareTimetable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
