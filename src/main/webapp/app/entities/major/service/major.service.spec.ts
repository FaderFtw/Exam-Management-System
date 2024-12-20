import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMajor } from '../major.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../major.test-samples';

import { MajorService } from './major.service';

const requireRestSample: IMajor = {
  ...sampleWithRequiredData,
};

describe('Major Service', () => {
  let service: MajorService;
  let httpMock: HttpTestingController;
  let expectedResult: IMajor | IMajor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MajorService);
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

    it('should create a Major', () => {
      const major = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(major).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Major', () => {
      const major = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(major).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Major', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Major', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Major', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMajorToCollectionIfMissing', () => {
      it('should add a Major to an empty array', () => {
        const major: IMajor = sampleWithRequiredData;
        expectedResult = service.addMajorToCollectionIfMissing([], major);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(major);
      });

      it('should not add a Major to an array that contains it', () => {
        const major: IMajor = sampleWithRequiredData;
        const majorCollection: IMajor[] = [
          {
            ...major,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMajorToCollectionIfMissing(majorCollection, major);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Major to an array that doesn't contain it", () => {
        const major: IMajor = sampleWithRequiredData;
        const majorCollection: IMajor[] = [sampleWithPartialData];
        expectedResult = service.addMajorToCollectionIfMissing(majorCollection, major);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(major);
      });

      it('should add only unique Major to an array', () => {
        const majorArray: IMajor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const majorCollection: IMajor[] = [sampleWithRequiredData];
        expectedResult = service.addMajorToCollectionIfMissing(majorCollection, ...majorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const major: IMajor = sampleWithRequiredData;
        const major2: IMajor = sampleWithPartialData;
        expectedResult = service.addMajorToCollectionIfMissing([], major, major2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(major);
        expect(expectedResult).toContain(major2);
      });

      it('should accept null and undefined values', () => {
        const major: IMajor = sampleWithRequiredData;
        expectedResult = service.addMajorToCollectionIfMissing([], null, major, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(major);
      });

      it('should return initial array if no Major is added', () => {
        const majorCollection: IMajor[] = [sampleWithRequiredData];
        expectedResult = service.addMajorToCollectionIfMissing(majorCollection, undefined, null);
        expect(expectedResult).toEqual(majorCollection);
      });
    });

    describe('compareMajor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMajor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMajor(entity1, entity2);
        const compareResult2 = service.compareMajor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMajor(entity1, entity2);
        const compareResult2 = service.compareMajor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMajor(entity1, entity2);
        const compareResult2 = service.compareMajor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
