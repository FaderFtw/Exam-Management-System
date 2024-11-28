import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';
import { TeachingSessionService } from 'app/entities/teaching-session/service/teaching-session.service';
import { IStudentClass } from '../student-class.model';
import { StudentClassService } from '../service/student-class.service';
import { StudentClassFormService } from './student-class-form.service';

import { StudentClassUpdateComponent } from './student-class-update.component';

describe('StudentClass Management Update Component', () => {
  let comp: StudentClassUpdateComponent;
  let fixture: ComponentFixture<StudentClassUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentClassFormService: StudentClassFormService;
  let studentClassService: StudentClassService;
  let examService: ExamService;
  let teachingSessionService: TeachingSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StudentClassUpdateComponent],
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
      .overrideTemplate(StudentClassUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentClassUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studentClassFormService = TestBed.inject(StudentClassFormService);
    studentClassService = TestBed.inject(StudentClassService);
    examService = TestBed.inject(ExamService);
    teachingSessionService = TestBed.inject(TeachingSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Exam query and add missing value', () => {
      const studentClass: IStudentClass = { id: 456 };
      const exam: IExam = { id: 25473 };
      studentClass.exam = exam;

      const examCollection: IExam[] = [{ id: 21649 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [exam];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TeachingSession query and add missing value', () => {
      const studentClass: IStudentClass = { id: 456 };
      const teachingSession: ITeachingSession = { id: 27300 };
      studentClass.teachingSession = teachingSession;

      const teachingSessionCollection: ITeachingSession[] = [{ id: 1854 }];
      jest.spyOn(teachingSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: teachingSessionCollection })));
      const additionalTeachingSessions = [teachingSession];
      const expectedCollection: ITeachingSession[] = [...additionalTeachingSessions, ...teachingSessionCollection];
      jest.spyOn(teachingSessionService, 'addTeachingSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      expect(teachingSessionService.query).toHaveBeenCalled();
      expect(teachingSessionService.addTeachingSessionToCollectionIfMissing).toHaveBeenCalledWith(
        teachingSessionCollection,
        ...additionalTeachingSessions.map(expect.objectContaining),
      );
      expect(comp.teachingSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const studentClass: IStudentClass = { id: 456 };
      const exam: IExam = { id: 11817 };
      studentClass.exam = exam;
      const teachingSession: ITeachingSession = { id: 28664 };
      studentClass.teachingSession = teachingSession;

      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      expect(comp.examsSharedCollection).toContain(exam);
      expect(comp.teachingSessionsSharedCollection).toContain(teachingSession);
      expect(comp.studentClass).toEqual(studentClass);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentClass>>();
      const studentClass = { id: 123 };
      jest.spyOn(studentClassFormService, 'getStudentClass').mockReturnValue(studentClass);
      jest.spyOn(studentClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentClass }));
      saveSubject.complete();

      // THEN
      expect(studentClassFormService.getStudentClass).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(studentClassService.update).toHaveBeenCalledWith(expect.objectContaining(studentClass));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentClass>>();
      const studentClass = { id: 123 };
      jest.spyOn(studentClassFormService, 'getStudentClass').mockReturnValue({ id: null });
      jest.spyOn(studentClassService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentClass: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentClass }));
      saveSubject.complete();

      // THEN
      expect(studentClassFormService.getStudentClass).toHaveBeenCalled();
      expect(studentClassService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStudentClass>>();
      const studentClass = { id: 123 };
      jest.spyOn(studentClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studentClassService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExam', () => {
      it('Should forward to examService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(examService, 'compareExam');
        comp.compareExam(entity, entity2);
        expect(examService.compareExam).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTeachingSession', () => {
      it('Should forward to teachingSessionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(teachingSessionService, 'compareTeachingSession');
        comp.compareTeachingSession(entity, entity2);
        expect(teachingSessionService.compareTeachingSession).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
