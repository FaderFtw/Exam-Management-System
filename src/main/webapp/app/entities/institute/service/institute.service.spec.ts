import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInstitute } from '../institute.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../institute.test-samples';

import { InstituteService } from './institute.service';

const requireRestSample: IInstitute = {
  ...sampleWithRequiredData,
};

describe('Institute Service', () => {
  let service: InstituteService;
  let httpMock: HttpTestingController;
  let expectedResult: IInstitute | IInstitute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InstituteService);
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

    it('should create a Institute', () => {
      const institute = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(institute).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Institute', () => {
      const institute = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(institute).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Institute', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Institute', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Institute', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInstituteToCollectionIfMissing', () => {
      it('should add a Institute to an empty array', () => {
        const institute: IInstitute = sampleWithRequiredData;
        expectedResult = service.addInstituteToCollectionIfMissing([], institute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institute);
      });

      it('should not add a Institute to an array that contains it', () => {
        const institute: IInstitute = sampleWithRequiredData;
        const instituteCollection: IInstitute[] = [
          {
            ...institute,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInstituteToCollectionIfMissing(instituteCollection, institute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Institute to an array that doesn't contain it", () => {
        const institute: IInstitute = sampleWithRequiredData;
        const instituteCollection: IInstitute[] = [sampleWithPartialData];
        expectedResult = service.addInstituteToCollectionIfMissing(instituteCollection, institute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institute);
      });

      it('should add only unique Institute to an array', () => {
        const instituteArray: IInstitute[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const instituteCollection: IInstitute[] = [sampleWithRequiredData];
        expectedResult = service.addInstituteToCollectionIfMissing(instituteCollection, ...instituteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const institute: IInstitute = sampleWithRequiredData;
        const institute2: IInstitute = sampleWithPartialData;
        expectedResult = service.addInstituteToCollectionIfMissing([], institute, institute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(institute);
        expect(expectedResult).toContain(institute2);
      });

      it('should accept null and undefined values', () => {
        const institute: IInstitute = sampleWithRequiredData;
        expectedResult = service.addInstituteToCollectionIfMissing([], null, institute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(institute);
      });

      it('should return initial array if no Institute is added', () => {
        const instituteCollection: IInstitute[] = [sampleWithRequiredData];
        expectedResult = service.addInstituteToCollectionIfMissing(instituteCollection, undefined, null);
        expect(expectedResult).toEqual(instituteCollection);
      });
    });

    describe('compareInstitute', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInstitute(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInstitute(entity1, entity2);
        const compareResult2 = service.compareInstitute(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInstitute(entity1, entity2);
        const compareResult2 = service.compareInstitute(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInstitute(entity1, entity2);
        const compareResult2 = service.compareInstitute(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
