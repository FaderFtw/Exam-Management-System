import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { IReport } from '../report.model';
import { ReportService } from '../service/report.service';
import { ReportFormService } from './report-form.service';

import { ReportUpdateComponent } from './report-update.component';

describe('Report Management Update Component', () => {
  let comp: ReportUpdateComponent;
  let fixture: ComponentFixture<ReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportFormService: ReportFormService;
  let reportService: ReportService;
  let professorDetailsService: ProfessorDetailsService;
  let examSessionService: ExamSessionService;
  let departmentService: DepartmentService;
  let instituteService: InstituteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReportUpdateComponent],
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
      .overrideTemplate(ReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportFormService = TestBed.inject(ReportFormService);
    reportService = TestBed.inject(ReportService);
    professorDetailsService = TestBed.inject(ProfessorDetailsService);
    examSessionService = TestBed.inject(ExamSessionService);
    departmentService = TestBed.inject(DepartmentService);
    instituteService = TestBed.inject(InstituteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProfessorDetails query and add missing value', () => {
      const report: IReport = { id: 456 };
      const professor: IProfessorDetails = { id: 7043 };
      report.professor = professor;

      const professorDetailsCollection: IProfessorDetails[] = [{ id: 4805 }];
      jest.spyOn(professorDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: professorDetailsCollection })));
      const additionalProfessorDetails = [professor];
      const expectedCollection: IProfessorDetails[] = [...additionalProfessorDetails, ...professorDetailsCollection];
      jest.spyOn(professorDetailsService, 'addProfessorDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(professorDetailsService.query).toHaveBeenCalled();
      expect(professorDetailsService.addProfessorDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        professorDetailsCollection,
        ...additionalProfessorDetails.map(expect.objectContaining),
      );
      expect(comp.professorDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExamSession query and add missing value', () => {
      const report: IReport = { id: 456 };
      const examSession: IExamSession = { id: 21826 };
      report.examSession = examSession;

      const examSessionCollection: IExamSession[] = [{ id: 8633 }];
      jest.spyOn(examSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: examSessionCollection })));
      const additionalExamSessions = [examSession];
      const expectedCollection: IExamSession[] = [...additionalExamSessions, ...examSessionCollection];
      jest.spyOn(examSessionService, 'addExamSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(examSessionService.query).toHaveBeenCalled();
      expect(examSessionService.addExamSessionToCollectionIfMissing).toHaveBeenCalledWith(
        examSessionCollection,
        ...additionalExamSessions.map(expect.objectContaining),
      );
      expect(comp.examSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const report: IReport = { id: 456 };
      const department: IDepartment = { id: 9794 };
      report.department = department;

      const departmentCollection: IDepartment[] = [{ id: 17054 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining),
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Institute query and add missing value', () => {
      const report: IReport = { id: 456 };
      const institute: IInstitute = { id: 27113 };
      report.institute = institute;

      const instituteCollection: IInstitute[] = [{ id: 25649 }];
      jest.spyOn(instituteService, 'query').mockReturnValue(of(new HttpResponse({ body: instituteCollection })));
      const additionalInstitutes = [institute];
      const expectedCollection: IInstitute[] = [...additionalInstitutes, ...instituteCollection];
      jest.spyOn(instituteService, 'addInstituteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(instituteService.query).toHaveBeenCalled();
      expect(instituteService.addInstituteToCollectionIfMissing).toHaveBeenCalledWith(
        instituteCollection,
        ...additionalInstitutes.map(expect.objectContaining),
      );
      expect(comp.institutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const report: IReport = { id: 456 };
      const professor: IProfessorDetails = { id: 25223 };
      report.professor = professor;
      const examSession: IExamSession = { id: 2072 };
      report.examSession = examSession;
      const department: IDepartment = { id: 13726 };
      report.department = department;
      const institute: IInstitute = { id: 11882 };
      report.institute = institute;

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(comp.professorDetailsSharedCollection).toContain(professor);
      expect(comp.examSessionsSharedCollection).toContain(examSession);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.institutesSharedCollection).toContain(institute);
      expect(comp.report).toEqual(report);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue(report);
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportService.update).toHaveBeenCalledWith(expect.objectContaining(report));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue({ id: null });
      jest.spyOn(reportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(reportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfessorDetails', () => {
      it('Should forward to professorDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(professorDetailsService, 'compareProfessorDetails');
        comp.compareProfessorDetails(entity, entity2);
        expect(professorDetailsService.compareProfessorDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExamSession', () => {
      it('Should forward to examSessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(examSessionService, 'compareExamSession');
        comp.compareExamSession(entity, entity2);
        expect(examSessionService.compareExamSession).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInstitute', () => {
      it('Should forward to instituteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(instituteService, 'compareInstitute');
        comp.compareInstitute(entity, entity2);
        expect(instituteService.compareInstitute).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
