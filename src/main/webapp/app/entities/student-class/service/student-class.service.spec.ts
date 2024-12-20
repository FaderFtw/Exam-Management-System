import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStudentClass } from '../student-class.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../student-class.test-samples';

import { StudentClassService } from './student-class.service';

const requireRestSample: IStudentClass = {
  ...sampleWithRequiredData,
};

describe('StudentClass Service', () => {
  let service: StudentClassService;
  let httpMock: HttpTestingController;
  let expectedResult: IStudentClass | IStudentClass[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StudentClassService);
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

    it('should create a StudentClass', () => {
      const studentClass = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(studentClass).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StudentClass', () => {
      const studentClass = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(studentClass).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StudentClass', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StudentClass', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StudentClass', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStudentClassToCollectionIfMissing', () => {
      it('should add a StudentClass to an empty array', () => {
        const studentClass: IStudentClass = sampleWithRequiredData;
        expectedResult = service.addStudentClassToCollectionIfMissing([], studentClass);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentClass);
      });

      it('should not add a StudentClass to an array that contains it', () => {
        const studentClass: IStudentClass = sampleWithRequiredData;
        const studentClassCollection: IStudentClass[] = [
          {
            ...studentClass,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStudentClassToCollectionIfMissing(studentClassCollection, studentClass);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StudentClass to an array that doesn't contain it", () => {
        const studentClass: IStudentClass = sampleWithRequiredData;
        const studentClassCollection: IStudentClass[] = [sampleWithPartialData];
        expectedResult = service.addStudentClassToCollectionIfMissing(studentClassCollection, studentClass);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentClass);
      });

      it('should add only unique StudentClass to an array', () => {
        const studentClassArray: IStudentClass[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const studentClassCollection: IStudentClass[] = [sampleWithRequiredData];
        expectedResult = service.addStudentClassToCollectionIfMissing(studentClassCollection, ...studentClassArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const studentClass: IStudentClass = sampleWithRequiredData;
        const studentClass2: IStudentClass = sampleWithPartialData;
        expectedResult = service.addStudentClassToCollectionIfMissing([], studentClass, studentClass2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentClass);
        expect(expectedResult).toContain(studentClass2);
      });

      it('should accept null and undefined values', () => {
        const studentClass: IStudentClass = sampleWithRequiredData;
        expectedResult = service.addStudentClassToCollectionIfMissing([], null, studentClass, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentClass);
      });

      it('should return initial array if no StudentClass is added', () => {
        const studentClassCollection: IStudentClass[] = [sampleWithRequiredData];
        expectedResult = service.addStudentClassToCollectionIfMissing(studentClassCollection, undefined, null);
        expect(expectedResult).toEqual(studentClassCollection);
      });
    });

    describe('compareStudentClass', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStudentClass(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStudentClass(entity1, entity2);
        const compareResult2 = service.compareStudentClass(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStudentClass(entity1, entity2);
        const compareResult2 = service.compareStudentClass(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStudentClass(entity1, entity2);
        const compareResult2 = service.compareStudentClass(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
