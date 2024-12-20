import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { MajorService } from '../service/major.service';
import { IMajor } from '../major.model';
import { MajorFormService } from './major-form.service';

import { MajorUpdateComponent } from './major-update.component';

describe('Major Management Update Component', () => {
  let comp: MajorUpdateComponent;
  let fixture: ComponentFixture<MajorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let majorFormService: MajorFormService;
  let majorService: MajorService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MajorUpdateComponent],
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
      .overrideTemplate(MajorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MajorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    majorFormService = TestBed.inject(MajorFormService);
    majorService = TestBed.inject(MajorService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const major: IMajor = { id: 456 };
      const department: IDepartment = { id: 1812 };
      major.department = department;

      const departmentCollection: IDepartment[] = [{ id: 32354 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ major });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining),
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const major: IMajor = { id: 456 };
      const department: IDepartment = { id: 15862 };
      major.department = department;

      activatedRoute.data = of({ major });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.major).toEqual(major);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMajor>>();
      const major = { id: 123 };
      jest.spyOn(majorFormService, 'getMajor').mockReturnValue(major);
      jest.spyOn(majorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ major });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: major }));
      saveSubject.complete();

      // THEN
      expect(majorFormService.getMajor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(majorService.update).toHaveBeenCalledWith(expect.objectContaining(major));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMajor>>();
      const major = { id: 123 };
      jest.spyOn(majorFormService, 'getMajor').mockReturnValue({ id: null });
      jest.spyOn(majorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ major: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: major }));
      saveSubject.complete();

      // THEN
      expect(majorFormService.getMajor).toHaveBeenCalled();
      expect(majorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMajor>>();
      const major = { id: 123 };
      jest.spyOn(majorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ major });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(majorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
