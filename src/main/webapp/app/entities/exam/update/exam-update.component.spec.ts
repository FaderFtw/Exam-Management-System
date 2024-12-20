import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { ExamSessionService } from 'app/entities/exam-session/service/exam-session.service';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { IExam } from '../exam.model';
import { ExamService } from '../service/exam.service';
import { ExamFormService } from './exam-form.service';

import { ExamUpdateComponent } from './exam-update.component';

describe('Exam Management Update Component', () => {
  let comp: ExamUpdateComponent;
  let fixture: ComponentFixture<ExamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let examFormService: ExamFormService;
  let examService: ExamService;
  let classroomService: ClassroomService;
  let studentClassService: StudentClassService;
  let examSessionService: ExamSessionService;
  let professorDetailsService: ProfessorDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExamUpdateComponent],
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
      .overrideTemplate(ExamUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    examFormService = TestBed.inject(ExamFormService);
    examService = TestBed.inject(ExamService);
    classroomService = TestBed.inject(ClassroomService);
    studentClassService = TestBed.inject(StudentClassService);
    examSessionService = TestBed.inject(ExamSessionService);
    professorDetailsService = TestBed.inject(ProfessorDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Classroom query and add missing value', () => {
      const exam: IExam = { id: 456 };
      const classroom: IClassroom = { id: 16858 };
      exam.classroom = classroom;

      const classroomCollection: IClassroom[] = [{ id: 22773 }];
      jest.spyOn(classroomService, 'query').mockReturnValue(of(new HttpResponse({ body: classroomCollection })));
      const additionalClassrooms = [classroom];
      const expectedCollection: IClassroom[] = [...additionalClassrooms, ...classroomCollection];
      jest.spyOn(classroomService, 'addClassroomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(classroomService.query).toHaveBeenCalled();
      expect(classroomService.addClassroomToCollectionIfMissing).toHaveBeenCalledWith(
        classroomCollection,
        ...additionalClassrooms.map(expect.objectContaining),
      );
      expect(comp.classroomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StudentClass query and add missing value', () => {
      const exam: IExam = { id: 456 };
      const studentClass: IStudentClass = { id: 29271 };
      exam.studentClass = studentClass;

      const studentClassCollection: IStudentClass[] = [{ id: 4028 }];
      jest.spyOn(studentClassService, 'query').mockReturnValue(of(new HttpResponse({ body: studentClassCollection })));
      const additionalStudentClasses = [studentClass];
      const expectedCollection: IStudentClass[] = [...additionalStudentClasses, ...studentClassCollection];
      jest.spyOn(studentClassService, 'addStudentClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(studentClassService.query).toHaveBeenCalled();
      expect(studentClassService.addStudentClassToCollectionIfMissing).toHaveBeenCalledWith(
        studentClassCollection,
        ...additionalStudentClasses.map(expect.objectContaining),
      );
      expect(comp.studentClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ExamSession query and add missing value', () => {
      const exam: IExam = { id: 456 };
      const session: IExamSession = { id: 4514 };
      exam.session = session;

      const examSessionCollection: IExamSession[] = [{ id: 28913 }];
      jest.spyOn(examSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: examSessionCollection })));
      const additionalExamSessions = [session];
      const expectedCollection: IExamSession[] = [...additionalExamSessions, ...examSessionCollection];
      jest.spyOn(examSessionService, 'addExamSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(examSessionService.query).toHaveBeenCalled();
      expect(examSessionService.addExamSessionToCollectionIfMissing).toHaveBeenCalledWith(
        examSessionCollection,
        ...additionalExamSessions.map(expect.objectContaining),
      );
      expect(comp.examSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProfessorDetails query and add missing value', () => {
      const exam: IExam = { id: 456 };
      const supervisors: IProfessorDetails[] = [{ id: 19600 }];
      exam.supervisors = supervisors;

      const professorDetailsCollection: IProfessorDetails[] = [{ id: 17927 }];
      jest.spyOn(professorDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: professorDetailsCollection })));
      const additionalProfessorDetails = [...supervisors];
      const expectedCollection: IProfessorDetails[] = [...additionalProfessorDetails, ...professorDetailsCollection];
      jest.spyOn(professorDetailsService, 'addProfessorDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(professorDetailsService.query).toHaveBeenCalled();
      expect(professorDetailsService.addProfessorDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        professorDetailsCollection,
        ...additionalProfessorDetails.map(expect.objectContaining),
      );
      expect(comp.professorDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exam: IExam = { id: 456 };
      const classroom: IClassroom = { id: 14619 };
      exam.classroom = classroom;
      const studentClass: IStudentClass = { id: 20786 };
      exam.studentClass = studentClass;
      const session: IExamSession = { id: 3149 };
      exam.session = session;
      const supervisors: IProfessorDetails = { id: 9850 };
      exam.supervisors = [supervisors];

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      expect(comp.classroomsSharedCollection).toContain(classroom);
      expect(comp.studentClassesSharedCollection).toContain(studentClass);
      expect(comp.examSessionsSharedCollection).toContain(session);
      expect(comp.professorDetailsSharedCollection).toContain(supervisors);
      expect(comp.exam).toEqual(exam);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 123 };
      jest.spyOn(examFormService, 'getExam').mockReturnValue(exam);
      jest.spyOn(examService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exam }));
      saveSubject.complete();

      // THEN
      expect(examFormService.getExam).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(examService.update).toHaveBeenCalledWith(expect.objectContaining(exam));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 123 };
      jest.spyOn(examFormService, 'getExam').mockReturnValue({ id: null });
      jest.spyOn(examService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exam }));
      saveSubject.complete();

      // THEN
      expect(examFormService.getExam).toHaveBeenCalled();
      expect(examService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExam>>();
      const exam = { id: 123 };
      jest.spyOn(examService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exam });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(examService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClassroom', () => {
      it('Should forward to classroomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classroomService, 'compareClassroom');
        comp.compareClassroom(entity, entity2);
        expect(classroomService.compareClassroom).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStudentClass', () => {
      it('Should forward to studentClassService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(studentClassService, 'compareStudentClass');
        comp.compareStudentClass(entity, entity2);
        expect(studentClassService.compareStudentClass).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareProfessorDetails', () => {
      it('Should forward to professorDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(professorDetailsService, 'compareProfessorDetails');
        comp.compareProfessorDetails(entity, entity2);
        expect(professorDetailsService.compareProfessorDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
