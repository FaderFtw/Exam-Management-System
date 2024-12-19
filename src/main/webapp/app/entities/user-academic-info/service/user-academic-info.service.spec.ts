import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserAcademicInfo } from '../user-academic-info.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-academic-info.test-samples';

import { UserAcademicInfoService } from './user-academic-info.service';

const requireRestSample: IUserAcademicInfo = {
  ...sampleWithRequiredData,
};

describe('UserAcademicInfo Service', () => {
  let service: UserAcademicInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserAcademicInfo | IUserAcademicInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserAcademicInfoService);
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

    it('should create a UserAcademicInfo', () => {
      const userAcademicInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userAcademicInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAcademicInfo', () => {
      const userAcademicInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userAcademicInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAcademicInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAcademicInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserAcademicInfo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserAcademicInfoToCollectionIfMissing', () => {
      it('should add a UserAcademicInfo to an empty array', () => {
        const userAcademicInfo: IUserAcademicInfo = sampleWithRequiredData;
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing([], userAcademicInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAcademicInfo);
      });

      it('should not add a UserAcademicInfo to an array that contains it', () => {
        const userAcademicInfo: IUserAcademicInfo = sampleWithRequiredData;
        const userAcademicInfoCollection: IUserAcademicInfo[] = [
          {
            ...userAcademicInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing(userAcademicInfoCollection, userAcademicInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAcademicInfo to an array that doesn't contain it", () => {
        const userAcademicInfo: IUserAcademicInfo = sampleWithRequiredData;
        const userAcademicInfoCollection: IUserAcademicInfo[] = [sampleWithPartialData];
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing(userAcademicInfoCollection, userAcademicInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAcademicInfo);
      });

      it('should add only unique UserAcademicInfo to an array', () => {
        const userAcademicInfoArray: IUserAcademicInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userAcademicInfoCollection: IUserAcademicInfo[] = [sampleWithRequiredData];
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing(userAcademicInfoCollection, ...userAcademicInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAcademicInfo: IUserAcademicInfo = sampleWithRequiredData;
        const userAcademicInfo2: IUserAcademicInfo = sampleWithPartialData;
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing([], userAcademicInfo, userAcademicInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAcademicInfo);
        expect(expectedResult).toContain(userAcademicInfo2);
      });

      it('should accept null and undefined values', () => {
        const userAcademicInfo: IUserAcademicInfo = sampleWithRequiredData;
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing([], null, userAcademicInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAcademicInfo);
      });

      it('should return initial array if no UserAcademicInfo is added', () => {
        const userAcademicInfoCollection: IUserAcademicInfo[] = [sampleWithRequiredData];
        expectedResult = service.addUserAcademicInfoToCollectionIfMissing(userAcademicInfoCollection, undefined, null);
        expect(expectedResult).toEqual(userAcademicInfoCollection);
      });
    });

    describe('compareUserAcademicInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserAcademicInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserAcademicInfo(entity1, entity2);
        const compareResult2 = service.compareUserAcademicInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserAcademicInfo(entity1, entity2);
        const compareResult2 = service.compareUserAcademicInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserAcademicInfo(entity1, entity2);
        const compareResult2 = service.compareUserAcademicInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
