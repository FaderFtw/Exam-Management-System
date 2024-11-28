import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IClassroom } from '../classroom.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../classroom.test-samples';

import { ClassroomService } from './classroom.service';

const requireRestSample: IClassroom = {
  ...sampleWithRequiredData,
};

describe('Classroom Service', () => {
  let service: ClassroomService;
  let httpMock: HttpTestingController;
  let expectedResult: IClassroom | IClassroom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ClassroomService);
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

    it('should create a Classroom', () => {
      const classroom = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(classroom).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Classroom', () => {
      const classroom = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(classroom).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Classroom', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Classroom', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Classroom', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClassroomToCollectionIfMissing', () => {
      it('should add a Classroom to an empty array', () => {
        const classroom: IClassroom = sampleWithRequiredData;
        expectedResult = service.addClassroomToCollectionIfMissing([], classroom);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classroom);
      });

      it('should not add a Classroom to an array that contains it', () => {
        const classroom: IClassroom = sampleWithRequiredData;
        const classroomCollection: IClassroom[] = [
          {
            ...classroom,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, classroom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Classroom to an array that doesn't contain it", () => {
        const classroom: IClassroom = sampleWithRequiredData;
        const classroomCollection: IClassroom[] = [sampleWithPartialData];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, classroom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classroom);
      });

      it('should add only unique Classroom to an array', () => {
        const classroomArray: IClassroom[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const classroomCollection: IClassroom[] = [sampleWithRequiredData];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, ...classroomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classroom: IClassroom = sampleWithRequiredData;
        const classroom2: IClassroom = sampleWithPartialData;
        expectedResult = service.addClassroomToCollectionIfMissing([], classroom, classroom2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classroom);
        expect(expectedResult).toContain(classroom2);
      });

      it('should accept null and undefined values', () => {
        const classroom: IClassroom = sampleWithRequiredData;
        expectedResult = service.addClassroomToCollectionIfMissing([], null, classroom, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classroom);
      });

      it('should return initial array if no Classroom is added', () => {
        const classroomCollection: IClassroom[] = [sampleWithRequiredData];
        expectedResult = service.addClassroomToCollectionIfMissing(classroomCollection, undefined, null);
        expect(expectedResult).toEqual(classroomCollection);
      });
    });

    describe('compareClassroom', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClassroom(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClassroom(entity1, entity2);
        const compareResult2 = service.compareClassroom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClassroom(entity1, entity2);
        const compareResult2 = service.compareClassroom(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClassroom(entity1, entity2);
        const compareResult2 = service.compareClassroom(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
