import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
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
  let instituteService: InstituteService;
  let examSessionService: ExamSessionService;

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
    instituteService = TestBed.inject(InstituteService);
    examSessionService = TestBed.inject(ExamSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Institute query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const institute: IInstitute = { id: 11673 };
      department.institute = institute;

      const instituteCollection: IInstitute[] = [{ id: 10883 }];
      jest.spyOn(instituteService, 'query').mockReturnValue(of(new HttpResponse({ body: instituteCollection })));
      const additionalInstitutes = [institute];
      const expectedCollection: IInstitute[] = [...additionalInstitutes, ...instituteCollection];
      jest.spyOn(instituteService, 'addInstituteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(instituteService.query).toHaveBeenCalled();
      expect(instituteService.addInstituteToCollectionIfMissing).toHaveBeenCalledWith(
        instituteCollection,
        ...additionalInstitutes.map(expect.objectContaining),
      );
      expect(comp.institutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExamSession query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const examSessions: IExamSession[] = [{ id: 22700 }];
      department.examSessions = examSessions;

      const examSessionCollection: IExamSession[] = [{ id: 2031 }];
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

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const institute: IInstitute = { id: 17704 };
      department.institute = institute;
      const examSessions: IExamSession = { id: 24832 };
      department.examSessions = [examSessions];

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.institutesSharedCollection).toContain(institute);
      expect(comp.examSessionsSharedCollection).toContain(examSessions);
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
    describe('compareInstitute', () => {
      it('Should forward to instituteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(instituteService, 'compareInstitute');
        comp.compareInstitute(entity, entity2);
        expect(instituteService.compareInstitute).toHaveBeenCalledWith(entity, entity2);
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
  });
});
