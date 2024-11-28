import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';
import { TeachingSessionService } from 'app/entities/teaching-session/service/teaching-session.service';
import { TimetableService } from '../service/timetable.service';
import { ITimetable } from '../timetable.model';
import { TimetableFormService } from './timetable-form.service';

import { TimetableUpdateComponent } from './timetable-update.component';

describe('Timetable Management Update Component', () => {
  let comp: TimetableUpdateComponent;
  let fixture: ComponentFixture<TimetableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let timetableFormService: TimetableFormService;
  let timetableService: TimetableService;
  let teachingSessionService: TeachingSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TimetableUpdateComponent],
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
      .overrideTemplate(TimetableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TimetableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    timetableFormService = TestBed.inject(TimetableFormService);
    timetableService = TestBed.inject(TimetableService);
    teachingSessionService = TestBed.inject(TeachingSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TeachingSession query and add missing value', () => {
      const timetable: ITimetable = { id: 456 };
      const teachingSession: ITeachingSession = { id: 4196 };
      timetable.teachingSession = teachingSession;

      const teachingSessionCollection: ITeachingSession[] = [{ id: 25923 }];
      jest.spyOn(teachingSessionService, 'query').mockReturnValue(of(new HttpResponse({ body: teachingSessionCollection })));
      const additionalTeachingSessions = [teachingSession];
      const expectedCollection: ITeachingSession[] = [...additionalTeachingSessions, ...teachingSessionCollection];
      jest.spyOn(teachingSessionService, 'addTeachingSessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timetable });
      comp.ngOnInit();

      expect(teachingSessionService.query).toHaveBeenCalled();
      expect(teachingSessionService.addTeachingSessionToCollectionIfMissing).toHaveBeenCalledWith(
        teachingSessionCollection,
        ...additionalTeachingSessions.map(expect.objectContaining),
      );
      expect(comp.teachingSessionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const timetable: ITimetable = { id: 456 };
      const teachingSession: ITeachingSession = { id: 23610 };
      timetable.teachingSession = teachingSession;

      activatedRoute.data = of({ timetable });
      comp.ngOnInit();

      expect(comp.teachingSessionsSharedCollection).toContain(teachingSession);
      expect(comp.timetable).toEqual(timetable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimetable>>();
      const timetable = { id: 123 };
      jest.spyOn(timetableFormService, 'getTimetable').mockReturnValue(timetable);
      jest.spyOn(timetableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timetable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timetable }));
      saveSubject.complete();

      // THEN
      expect(timetableFormService.getTimetable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(timetableService.update).toHaveBeenCalledWith(expect.objectContaining(timetable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimetable>>();
      const timetable = { id: 123 };
      jest.spyOn(timetableFormService, 'getTimetable').mockReturnValue({ id: null });
      jest.spyOn(timetableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timetable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timetable }));
      saveSubject.complete();

      // THEN
      expect(timetableFormService.getTimetable).toHaveBeenCalled();
      expect(timetableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimetable>>();
      const timetable = { id: 123 };
      jest.spyOn(timetableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timetable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(timetableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
