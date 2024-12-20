import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IReport } from '../report.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../report.test-samples';

import { ReportService, RestReport } from './report.service';

const requireRestSample: RestReport = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
};

describe('Report Service', () => {
  let service: ReportService;
  let httpMock: HttpTestingController;
  let expectedResult: IReport | IReport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReportService);
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

    it('should create a Report', () => {
      const report = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(report).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Report', () => {
      const report = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(report).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Report', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Report', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Report', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addReportToCollectionIfMissing', () => {
      it('should add a Report to an empty array', () => {
        const report: IReport = sampleWithRequiredData;
        expectedResult = service.addReportToCollectionIfMissing([], report);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(report);
      });

      it('should not add a Report to an array that contains it', () => {
        const report: IReport = sampleWithRequiredData;
        const reportCollection: IReport[] = [
          {
            ...report,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReportToCollectionIfMissing(reportCollection, report);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Report to an array that doesn't contain it", () => {
        const report: IReport = sampleWithRequiredData;
        const reportCollection: IReport[] = [sampleWithPartialData];
        expectedResult = service.addReportToCollectionIfMissing(reportCollection, report);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(report);
      });

      it('should add only unique Report to an array', () => {
        const reportArray: IReport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const reportCollection: IReport[] = [sampleWithRequiredData];
        expectedResult = service.addReportToCollectionIfMissing(reportCollection, ...reportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const report: IReport = sampleWithRequiredData;
        const report2: IReport = sampleWithPartialData;
        expectedResult = service.addReportToCollectionIfMissing([], report, report2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(report);
        expect(expectedResult).toContain(report2);
      });

      it('should accept null and undefined values', () => {
        const report: IReport = sampleWithRequiredData;
        expectedResult = service.addReportToCollectionIfMissing([], null, report, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(report);
      });

      it('should return initial array if no Report is added', () => {
        const reportCollection: IReport[] = [sampleWithRequiredData];
        expectedResult = service.addReportToCollectionIfMissing(reportCollection, undefined, null);
        expect(expectedResult).toEqual(reportCollection);
      });
    });

    describe('compareReport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareReport(entity1, entity2);
        const compareResult2 = service.compareReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareReport(entity1, entity2);
        const compareResult2 = service.compareReport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareReport(entity1, entity2);
        const compareResult2 = service.compareReport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
