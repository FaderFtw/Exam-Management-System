import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';
import { TeachingSessionService } from 'app/entities/teaching-session/service/teaching-session.service';
import { IClassroom } from '../classroom.model';
import { ClassroomService } from '../service/classroom.service';
import { ClassroomFormService } from './classroom-form.service';

import { ClassroomUpdateComponent } from './classroom-update.component';

describe('Classroom Management Update Component', () => {
  let comp: ClassroomUpdateComponent;
  let fixture: ComponentFixture<ClassroomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classroomFormService: ClassroomFormService;
  let classroomService: ClassroomService;
  let examService: ExamService;
  let teachingSessionService: TeachingSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClassroomUpdateComponent],
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
      .overrideTemplate(ClassroomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassroomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classroomFormService = TestBed.inject(ClassroomFormService);
    classroomService = TestBed.inject(ClassroomService);
    examService = TestBed.inject(ExamService);
    teachingSessionService = TestBed.inject(TeachingSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Exam query and add missing value', () => {
      const classroom: IClassroom = { id: 456 };
      const exam: IExam = { id: 9507 };
      classroom.exam = exam;

      const examCollection: IExam[] = [{ id: 27762 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [exam];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TeachingSession query and add missing value', () => {
      const classroom: IClassroom = { id: 456 };
      const teachingSession: ITeachingSession = { id: 8305 };
      classroom.teachingSession = teachingSession;

      const teachingSessionCollection: ITeachingSession[] = [{ id: 1523 }];
      jest.spyOn(teachingSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: teachingSessionCollection })));
      const additionalTeachingSessions = [teachingSession];
      const expectedCollection: ITeachingSession[] = [...additionalTeachingSessions, ...teachingSessionCollection];
      jest.spyOn(teachingSessionService, 'addTeachingSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      expect(teachingSessionService.query).toHaveBeenCalled();
      expect(teachingSessionService.addTeachingSessionToCollectionIfMissing).toHaveBeenCalledWith(
        teachingSessionCollection,
        ...additionalTeachingSessions.map(expect.objectContaining),
      );
      expect(comp.teachingSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classroom: IClassroom = { id: 456 };
      const exam: IExam = { id: 25618 };
      classroom.exam = exam;
      const teachingSession: ITeachingSession = { id: 5137 };
      classroom.teachingSession = teachingSession;

      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      expect(comp.examsSharedCollection).toContain(exam);
      expect(comp.teachingSessionsSharedCollection).toContain(teachingSession);
      expect(comp.classroom).toEqual(classroom);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassroom>>();
      const classroom = { id: 123 };
      jest.spyOn(classroomFormService, 'getClassroom').mockReturnValue(classroom);
      jest.spyOn(classroomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classroom }));
      saveSubject.complete();

      // THEN
      expect(classroomFormService.getClassroom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(classroomService.update).toHaveBeenCalledWith(expect.objectContaining(classroom));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassroom>>();
      const classroom = { id: 123 };
      jest.spyOn(classroomFormService, 'getClassroom').mockReturnValue({ id: null });
      jest.spyOn(classroomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classroom }));
      saveSubject.complete();

      // THEN
      expect(classroomFormService.getClassroom).toHaveBeenCalled();
      expect(classroomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IClassroom>>();
      const classroom = { id: 123 };
      jest.spyOn(classroomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classroom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classroomService.update).toHaveBeenCalled();
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
