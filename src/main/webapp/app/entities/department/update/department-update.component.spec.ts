import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { IMajor } from 'app/entities/major/major.model';
import { MajorService } from 'app/entities/major/service/major.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';
import { IDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { DepartmentFormService } from './department-form.service';

import { DepartmentUpdateComponent } from './department-update.component';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdateComponent;
  let fixture: ComponentFixture<DepartmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentFormService: DepartmentFormService;
  let departmentService: DepartmentService;
  let userService: UserService;
  let examSessionService: ExamSessionService;
  let classroomService: ClassroomService;
  let majorService: MajorService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DepartmentUpdateComponent],
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
      .overrideTemplate(DepartmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentFormService = TestBed.inject(DepartmentFormService);
    departmentService = TestBed.inject(DepartmentService);
    userService = TestBed.inject(UserService);
    examSessionService = TestBed.inject(ExamSessionService);
    classroomService = TestBed.inject(ClassroomService);
    majorService = TestBed.inject(MajorService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const users: IUser = { id: 16803 };
      department.users = users;

      const userCollection: IUser[] = [{ id: 14122 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [users];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExamSession query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const examSessions: IExamSession[] = [{ id: 7168 }];
      department.examSessions = examSessions;

      const examSessionCollection: IExamSession[] = [{ id: 2609 }];
      jest.spyOn(examSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: examSessionCollection })));
      const additionalExamSessions = [...examSessions];
      const expectedCollection: IExamSession[] = [...additionalExamSessions, ...examSessionCollection];
      jest.spyOn(examSessionService, 'addExamSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(examSessionService.query).toHaveBeenCalled();
      expect(examSessionService.addExamSessionToCollectionIfMissing).toHaveBeenCalledWith(
        examSessionCollection,
        ...additionalExamSessions.map(expect.objectContaining),
      );
      expect(comp.examSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Classroom query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const classroom: IClassroom = { id: 1519 };
      department.classroom = classroom;

      const classroomCollection: IClassroom[] = [{ id: 24211 }];
      jest.spyOn(classroomService, 'query').mockReturnValue(of(new HttpResponse({ body: classroomCollection })));
      const additionalClassrooms = [classroom];
      const expectedCollection: IClassroom[] = [...additionalClassrooms, ...classroomCollection];
      jest.spyOn(classroomService, 'addClassroomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(classroomService.query).toHaveBeenCalled();
      expect(classroomService.addClassroomToCollectionIfMissing).toHaveBeenCalledWith(
        classroomCollection,
        ...additionalClassrooms.map(expect.objectContaining),
      );
      expect(comp.classroomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Major query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const major: IMajor = { id: 2294 };
      department.major = major;

      const majorCollection: IMajor[] = [{ id: 12210 }];
      jest.spyOn(majorService, 'query').mockReturnValue(of(new HttpResponse({ body: majorCollection })));
      const additionalMajors = [major];
      const expectedCollection: IMajor[] = [...additionalMajors, ...majorCollection];
      jest.spyOn(majorService, 'addMajorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(majorService.query).toHaveBeenCalled();
      expect(majorService.addMajorToCollectionIfMissing).toHaveBeenCalledWith(
        majorCollection,
        ...additionalMajors.map(expect.objectContaining),
      );
      expect(comp.majorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Report query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const report: IReport = { id: 14573 };
      department.report = report;

      const reportCollection: IReport[] = [{ id: 28446 }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const users: IUser = { id: 8313 };
      department.users = users;
      const examSessions: IExamSession = { id: 9558 };
      department.examSessions = [examSessions];
      const classroom: IClassroom = { id: 20202 };
      department.classroom = classroom;
      const major: IMajor = { id: 2255 };
      department.major = major;
      const report: IReport = { id: 19895 };
      department.report = report;

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(users);
      expect(comp.examSessionsSharedCollection).toContain(examSessions);
      expect(comp.classroomsSharedCollection).toContain(classroom);
      expect(comp.majorsSharedCollection).toContain(major);
      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.department).toEqual(department);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentFormService, 'getDepartment').mockReturnValue(department);
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(expect.objectContaining(department));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentFormService, 'getDepartment').mockReturnValue({ id: null });
      jest.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentFormService.getDepartment).toHaveBeenCalled();
      expect(departmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartment>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareClassroom', () => {
      it('Should forward to classroomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classroomService, 'compareClassroom');
        comp.compareClassroom(entity, entity2);
        expect(classroomService.compareClassroom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMajor', () => {
      it('Should forward to majorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(majorService, 'compareMajor');
        comp.compareMajor(entity, entity2);
        expect(majorService.compareMajor).toHaveBeenCalledWith(entity, entity2);
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
