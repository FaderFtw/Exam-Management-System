import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProfessorDetails } from '../professor-details.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../professor-details.test-samples';

import { ProfessorDetailsService } from './professor-details.service';

const requireRestSample: IProfessorDetails = {
  ...sampleWithRequiredData,
};

describe('ProfessorDetails Service', () => {
  let service: ProfessorDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IProfessorDetails | IProfessorDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProfessorDetailsService);
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

    it('should create a ProfessorDetails', () => {
      const professorDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(professorDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProfessorDetails', () => {
      const professorDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(professorDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProfessorDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProfessorDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProfessorDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProfessorDetailsToCollectionIfMissing', () => {
      it('should add a ProfessorDetails to an empty array', () => {
        const professorDetails: IProfessorDetails = sampleWithRequiredData;
        expectedResult = service.addProfessorDetailsToCollectionIfMissing([], professorDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professorDetails);
      });

      it('should not add a ProfessorDetails to an array that contains it', () => {
        const professorDetails: IProfessorDetails = sampleWithRequiredData;
        const professorDetailsCollection: IProfessorDetails[] = [
          {
            ...professorDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProfessorDetailsToCollectionIfMissing(professorDetailsCollection, professorDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProfessorDetails to an array that doesn't contain it", () => {
        const professorDetails: IProfessorDetails = sampleWithRequiredData;
        const professorDetailsCollection: IProfessorDetails[] = [sampleWithPartialData];
        expectedResult = service.addProfessorDetailsToCollectionIfMissing(professorDetailsCollection, professorDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professorDetails);
      });

      it('should add only unique ProfessorDetails to an array', () => {
        const professorDetailsArray: IProfessorDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const professorDetailsCollection: IProfessorDetails[] = [sampleWithRequiredData];
        expectedResult = service.addProfessorDetailsToCollectionIfMissing(professorDetailsCollection, ...professorDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const professorDetails: IProfessorDetails = sampleWithRequiredData;
        const professorDetails2: IProfessorDetails = sampleWithPartialData;
        expectedResult = service.addProfessorDetailsToCollectionIfMissing([], professorDetails, professorDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(professorDetails);
        expect(expectedResult).toContain(professorDetails2);
      });

      it('should accept null and undefined values', () => {
        const professorDetails: IProfessorDetails = sampleWithRequiredData;
        expectedResult = service.addProfessorDetailsToCollectionIfMissing([], null, professorDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(professorDetails);
      });

      it('should return initial array if no ProfessorDetails is added', () => {
        const professorDetailsCollection: IProfessorDetails[] = [sampleWithRequiredData];
        expectedResult = service.addProfessorDetailsToCollectionIfMissing(professorDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(professorDetailsCollection);
      });
    });

    describe('compareProfessorDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProfessorDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProfessorDetails(entity1, entity2);
        const compareResult2 = service.compareProfessorDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProfessorDetails(entity1, entity2);
        const compareResult2 = service.compareProfessorDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProfessorDetails(entity1, entity2);
        const compareResult2 = service.compareProfessorDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
