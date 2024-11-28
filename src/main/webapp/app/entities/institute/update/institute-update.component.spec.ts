import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IInstitute } from '../institute.model';
import { InstituteService } from '../service/institute.service';
import { InstituteFormService } from './institute-form.service';

import { InstituteUpdateComponent } from './institute-update.component';

describe('Institute Management Update Component', () => {
  let comp: InstituteUpdateComponent;
  let fixture: ComponentFixture<InstituteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instituteFormService: InstituteFormService;
  let instituteService: InstituteService;
  let departmentService: DepartmentService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InstituteUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InstituteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstituteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instituteFormService = TestBed.inject(InstituteFormService);
    instituteService = TestBed.inject(InstituteService);
    departmentService = TestBed.inject(DepartmentService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const institute: IInstitute = { id: 456 };
      const department: IDepartment = { id: 9230 };
      institute.department = department;

      const departmentCollection: IDepartment[] = [{ id: 24495 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining),
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Report query and add missing value', () => {
      const institute: IInstitute = { id: 456 };
      const report: IReport = { id: 13657 };
      institute.report = report;

      const reportCollection: IReport[] = [{ id: 25880 }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const institute: IInstitute = { id: 456 };
      const department: IDepartment = { id: 1663 };
      institute.department = department;
      const report: IReport = { id: 12203 };
      institute.report = report;

      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.institute).toEqual(institute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteFormService, 'getInstitute').mockReturnValue(institute);
      jest.spyOn(instituteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institute }));
      saveSubject.complete();

      // THEN
      expect(instituteFormService.getInstitute).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(instituteService.update).toHaveBeenCalledWith(expect.objectContaining(institute));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteFormService, 'getInstitute').mockReturnValue({ id: null });
      jest.spyOn(instituteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institute }));
      saveSubject.complete();

      // THEN
      expect(instituteFormService.getInstitute).toHaveBeenCalled();
      expect(instituteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instituteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReport', () => {
      it('Should forward to reportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(reportService, 'compareReport');
        comp.compareReport(entity, entity2);
        expect(reportService.compareReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
