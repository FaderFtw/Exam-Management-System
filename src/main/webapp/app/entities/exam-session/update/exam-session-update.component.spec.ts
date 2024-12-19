import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISessionType } from 'app/entities/session-type/session-type.model';
import { SessionTypeService } from 'app/entities/session-type/service/session-type.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call sessionType query and add missing value', () => {
      const examSession: IExamSession = { id: 456 };
      const sessionType: ISessionType = { id: 26420 };
      examSession.sessionType = sessionType;

      const sessionTypeCollection: ISessionType[] = [{ id: 9331 }];
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
      const departments: IDepartment[] = [{ id: 3999 }];
      examSession.departments = departments;

      const departmentCollection: IDepartment[] = [{ id: 5757 }];
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

    it('Should update editForm', () => {
      const examSession: IExamSession = { id: 456 };
      const sessionType: ISessionType = { id: 3315 };
      examSession.sessionType = sessionType;
      const departments: IDepartment = { id: 18099 };
      examSession.departments = [departments];

      activatedRoute.data = of({ examSession });
      comp.ngOnInit();

      expect(comp.sessionTypesCollection).toContain(sessionType);
      expect(comp.departmentsSharedCollection).toContain(departments);
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
  });
});
