import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TeachingSessionService } from '../service/teaching-session.service';
import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionFormService } from './teaching-session-form.service';

import { TeachingSessionUpdateComponent } from './teaching-session-update.component';

describe('TeachingSession Management Update Component', () => {
  let comp: TeachingSessionUpdateComponent;
  let fixture: ComponentFixture<TeachingSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teachingSessionFormService: TeachingSessionFormService;
  let teachingSessionService: TeachingSessionService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const teachingSession: ITeachingSession = { id: 456 };

      activatedRoute.data = of({ teachingSession });
      comp.ngOnInit();

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
});
