import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SessionTypeService } from '../service/session-type.service';
import { ISessionType } from '../session-type.model';
import { SessionTypeFormService } from './session-type-form.service';

import { SessionTypeUpdateComponent } from './session-type-update.component';

describe('SessionType Management Update Component', () => {
  let comp: SessionTypeUpdateComponent;
  let fixture: ComponentFixture<SessionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionTypeFormService: SessionTypeFormService;
  let sessionTypeService: SessionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SessionTypeUpdateComponent],
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
      .overrideTemplate(SessionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionTypeFormService = TestBed.inject(SessionTypeFormService);
    sessionTypeService = TestBed.inject(SessionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sessionType: ISessionType = { id: 456 };

      activatedRoute.data = of({ sessionType });
      comp.ngOnInit();

      expect(comp.sessionType).toEqual(sessionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionType>>();
      const sessionType = { id: 123 };
      jest.spyOn(sessionTypeFormService, 'getSessionType').mockReturnValue(sessionType);
      jest.spyOn(sessionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionType }));
      saveSubject.complete();

      // THEN
      expect(sessionTypeFormService.getSessionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(sessionType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionType>>();
      const sessionType = { id: 123 };
      jest.spyOn(sessionTypeFormService, 'getSessionType').mockReturnValue({ id: null });
      jest.spyOn(sessionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sessionType }));
      saveSubject.complete();

      // THEN
      expect(sessionTypeFormService.getSessionType).toHaveBeenCalled();
      expect(sessionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISessionType>>();
      const sessionType = { id: 123 };
      jest.spyOn(sessionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sessionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
