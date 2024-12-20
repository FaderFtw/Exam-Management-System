import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { InstituteService } from '../service/institute.service';
import { IInstitute } from '../institute.model';
import { InstituteFormService } from './institute-form.service';

import { InstituteUpdateComponent } from './institute-update.component';

describe('Institute Management Update Component', () => {
  let comp: InstituteUpdateComponent;
  let fixture: ComponentFixture<InstituteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let instituteFormService: InstituteFormService;
  let instituteService: InstituteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InstituteUpdateComponent],
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
      .overrideTemplate(InstituteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstituteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    instituteFormService = TestBed.inject(InstituteFormService);
    instituteService = TestBed.inject(InstituteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const institute: IInstitute = { id: 456 };

      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      expect(comp.institute).toEqual(institute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteFormService, 'getInstitute').mockReturnValue(institute);
      jest.spyOn(instituteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institute }));
      saveSubject.complete();

      // THEN
      expect(instituteFormService.getInstitute).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(instituteService.update).toHaveBeenCalledWith(expect.objectContaining(institute));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteFormService, 'getInstitute').mockReturnValue({ id: null });
      jest.spyOn(instituteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institute }));
      saveSubject.complete();

      // THEN
      expect(instituteFormService.getInstitute).toHaveBeenCalled();
      expect(instituteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInstitute>>();
      const institute = { id: 123 };
      jest.spyOn(instituteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(instituteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
