import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISessionType } from 'app/entities/session-type/session-type.model';
import { SessionTypeService } from 'app/entities/session-type/service/session-type.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IExamSession } from '../exam-session.model';
import { ExamSessionService } from '../service/exam-session.service';
import { ExamSessionFormService } from './exam-session-form.service';

import { ExamSessionUpdateComponent } from './exam-session-update.component';

describe('ExamSession Management Update Component', () => {
  let comp: ExamSessionUpdateComponent;
  let fixture: ComponentFixture<ExamSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let examSessionFormService: ExamSessionFormService;
  let examSessionService: ExamSessionService;
  let sessionTypeService: SessionTypeService;
  let departmentService: DepartmentService;
  let examService: ExamService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExamSessionUpdateComponent],
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
      .overrideTemplate(ExamSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExamSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    examSessionFormService = TestBed.inject(ExamSessionFormService);
    examSessionService = TestBed.inject(ExamSessionService);
    sessionTypeService = TestBed.inject(SessionTypeService);
    departmentService = TestBed.inject(DepartmentService);
    examService = TestBed.inject(ExamService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call sessionType query and add missing value', () => {
      const examSession: IExamSession = { id: 456 };
      const sessionType: ISessionType = { id: 4579 };
      examSession.sessionType = sessionType;

      const sessionTypeCollection: ISessionType[] = [{ id: 14040 }];
      jest.spyOn(sessionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: sessionTypeCollection })));
      const expectedCollection: ISessionType[] = [sessionType, ...sessionTypeCollection];
      jest.spyOn(sessionTypeService, 'addSessionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(sessionTypeService.query).toHaveBeenCalled();
      expect(sessionTypeService.addSessionTypeToCollectionIfMissing).toHaveBeenCalledWith(sessionTypeCollection, sessionType);
      expect(comp.sessionTypesCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const examSession: IExamSession = { id: 456 };
      const departments: IDepartment[] = [{ id: 8320 }];
      examSession.departments = departments;

      const departmentCollection: IDepartment[] = [{ id: 22286 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [...departments];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining),
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Exam query and add missing value', () => {
      const examSession: IExamSession = { id: 456 };
      const exam: IExam = { id: 22615 };
      examSession.exam = exam;

      const examCollection: IExam[] = [{ id: 12152 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [exam];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Report query and add missing value', () => {
      const examSession: IExamSession = { id: 456 };
      const report: IReport = { id: 24568 };
      examSession.report = report;

      const reportCollection: IReport[] = [{ id: 17151 }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const examSession: IExamSession = { id: 456 };
      const sessionType: ISessionType = { id: 10285 };
      examSession.sessionType = sessionType;
      const departments: IDepartment = { id: 10116 };
      examSession.departments = [departments];
      const exam: IExam = { id: 9458 };
      examSession.exam = exam;
      const report: IReport = { id: 4166 };
      examSession.report = report;

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(comp.sessionTypesCollection).toContain(sessionType);
      expect(comp.departmentsSharedCollection).toContain(departments);
      expect(comp.examsSharedCollection).toContain(exam);
      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.examSession).toEqual(examSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExamSession>>();
      const examSession = { id: 123 };
      jest.spyOn(examSessionFormService, 'getExamSession').mockReturnValue(examSession);
      jest.spyOn(examSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: examSession }));
      saveSubject.complete();

      // THEN
      expect(examSessionFormService.getExamSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(examSessionService.update).toHaveBeenCalledWith(expect.objectContaining(examSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExamSession>>();
      const examSession = { id: 123 };
      jest.spyOn(examSessionFormService, 'getExamSession').mockReturnValue({ id: null });
      jest.spyOn(examSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: examSession }));
      saveSubject.complete();

      // THEN
      expect(examSessionFormService.getExamSession).toHaveBeenCalled();
      expect(examSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExamSession>>();
      const examSession = { id: 123 };
      jest.spyOn(examSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(examSessionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSessionType', () => {
      it('Should forward to sessionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sessionTypeService, 'compareSessionType');
        comp.compareSessionType(entity, entity2);
        expect(sessionTypeService.compareSessionType).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareExam', () => {
      it('Should forward to examService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(examService, 'compareExam');
        comp.compareExam(entity, entity2);
        expect(examService.compareExam).toHaveBeenCalledWith(entity, entity2);
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
