import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITimetable } from 'app/entities/timetable/timetable.model';
import { TimetableService } from 'app/entities/timetable/service/timetable.service';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { ClassroomService } from 'app/entities/classroom/service/classroom.service';
import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionService } from '../service/teaching-session.service';
import { TeachingSessionFormService } from './teaching-session-form.service';

import { TeachingSessionUpdateComponent } from './teaching-session-update.component';

describe('TeachingSession Management Update Component', () => {
  let comp: TeachingSessionUpdateComponent;
  let fixture: ComponentFixture<TeachingSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teachingSessionFormService: TeachingSessionFormService;
  let teachingSessionService: TeachingSessionService;
  let timetableService: TimetableService;
  let studentClassService: StudentClassService;
  let classroomService: ClassroomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TeachingSessionUpdateComponent],
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
      .overrideTemplate(TeachingSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeachingSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teachingSessionFormService = TestBed.inject(TeachingSessionFormService);
    teachingSessionService = TestBed.inject(TeachingSessionService);
    timetableService = TestBed.inject(TimetableService);
    studentClassService = TestBed.inject(StudentClassService);
    classroomService = TestBed.inject(ClassroomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Timetable query and add missing value', () => {
      const teachingSession: ITeachingSession = { id: 456 };
      const timetable: ITimetable = { id: 22589 };
      teachingSession.timetable = timetable;

      const timetableCollection: ITimetable[] = [{ id: 31666 }];
      jest.spyOn(timetableService, 'query').mockReturnValue(of(new HttpResponse({ body: timetableCollection })));
      const additionalTimetables = [timetable];
      const expectedCollection: ITimetable[] = [...additionalTimetables, ...timetableCollection];
      jest.spyOn(timetableService, 'addTimetableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      expect(timetableService.query).toHaveBeenCalled();
      expect(timetableService.addTimetableToCollectionIfMissing).toHaveBeenCalledWith(
        timetableCollection,
        ...additionalTimetables.map(expect.objectContaining),
      );
      expect(comp.timetablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StudentClass query and add missing value', () => {
      const teachingSession: ITeachingSession = { id: 456 };
      const studentClass: IStudentClass = { id: 5864 };
      teachingSession.studentClass = studentClass;

      const studentClassCollection: IStudentClass[] = [{ id: 634 }];
      jest.spyOn(studentClassService, 'query').mockReturnValue(of(new HttpResponse({ body: studentClassCollection })));
      const additionalStudentClasses = [studentClass];
      const expectedCollection: IStudentClass[] = [...additionalStudentClasses, ...studentClassCollection];
      jest.spyOn(studentClassService, 'addStudentClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      expect(studentClassService.query).toHaveBeenCalled();
      expect(studentClassService.addStudentClassToCollectionIfMissing).toHaveBeenCalledWith(
        studentClassCollection,
        ...additionalStudentClasses.map(expect.objectContaining),
      );
      expect(comp.studentClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Classroom query and add missing value', () => {
      const teachingSession: ITeachingSession = { id: 456 };
      const classroom: IClassroom = { id: 4137 };
      teachingSession.classroom = classroom;

      const classroomCollection: IClassroom[] = [{ id: 30411 }];
      jest.spyOn(classroomService, 'query').mockReturnValue(of(new HttpResponse({ body: classroomCollection })));
      const additionalClassrooms = [classroom];
      const expectedCollection: IClassroom[] = [...additionalClassrooms, ...classroomCollection];
      jest.spyOn(classroomService, 'addClassroomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      expect(classroomService.query).toHaveBeenCalled();
      expect(classroomService.addClassroomToCollectionIfMissing).toHaveBeenCalledWith(
        classroomCollection,
        ...additionalClassrooms.map(expect.objectContaining),
      );
      expect(comp.classroomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const teachingSession: ITeachingSession = { id: 456 };
      const timetable: ITimetable = { id: 11662 };
      teachingSession.timetable = timetable;
      const studentClass: IStudentClass = { id: 8233 };
      teachingSession.studentClass = studentClass;
      const classroom: IClassroom = { id: 25376 };
      teachingSession.classroom = classroom;

      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      expect(comp.timetablesSharedCollection).toContain(timetable);
      expect(comp.studentClassesSharedCollection).toContain(studentClass);
      expect(comp.classroomsSharedCollection).toContain(classroom);
      expect(comp.teachingSession).toEqual(teachingSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachingSession>>();
      const teachingSession = { id: 123 };
      jest.spyOn(teachingSessionFormService, 'getTeachingSession').mockReturnValue(teachingSession);
      jest.spyOn(teachingSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teachingSession }));
      saveSubject.complete();

      // THEN
      expect(teachingSessionFormService.getTeachingSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(teachingSessionService.update).toHaveBeenCalledWith(expect.objectContaining(teachingSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachingSession>>();
      const teachingSession = { id: 123 };
      jest.spyOn(teachingSessionFormService, 'getTeachingSession').mockReturnValue({ id: null });
      jest.spyOn(teachingSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachingSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teachingSession }));
      saveSubject.complete();

      // THEN
      expect(teachingSessionFormService.getTeachingSession).toHaveBeenCalled();
      expect(teachingSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITeachingSession>>();
      const teachingSession = { id: 123 };
      jest.spyOn(teachingSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teachingSessionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTimetable', () => {
      it('Should forward to timetableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(timetableService, 'compareTimetable');
        comp.compareTimetable(entity, entity2);
        expect(timetableService.compareTimetable).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareClassroom', () => {
      it('Should forward to classroomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classroomService, 'compareClassroom');
        comp.compareClassroom(entity, entity2);
        expect(classroomService.compareClassroom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
