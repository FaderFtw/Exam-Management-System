import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMajor } from 'app/entities/major/major.model';
import { MajorService } from 'app/entities/major/service/major.service';
import { StudentClassService } from '../service/student-class.service';
import { IStudentClass } from '../student-class.model';
import { StudentClassFormService } from './student-class-form.service';

import { StudentClassUpdateComponent } from './student-class-update.component';

describe('StudentClass Management Update Component', () => {
  let comp: StudentClassUpdateComponent;
  let fixture: ComponentFixture<StudentClassUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentClassFormService: StudentClassFormService;
  let studentClassService: StudentClassService;
  let majorService: MajorService;

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
    majorService = TestBed.inject(MajorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Major query and add missing value', () => {
      const studentClass: IStudentClass = { id: 456 };
      const major: IMajor = { id: 10248 };
      studentClass.major = major;

      const majorCollection: IMajor[] = [{ id: 22315 }];
      jest.spyOn(majorService, 'query').mockReturnValue(of(new HttpResponse({ body: majorCollection })));
      const additionalMajors = [major];
      const expectedCollection: IMajor[] = [...additionalMajors, ...majorCollection];
      jest.spyOn(majorService, 'addMajorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      expect(majorService.query).toHaveBeenCalled();
      expect(majorService.addMajorToCollectionIfMissing).toHaveBeenCalledWith(
        majorCollection,
        ...additionalMajors.map(expect.objectContaining),
      );
      expect(comp.majorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const studentClass: IStudentClass = { id: 456 };
      const major: IMajor = { id: 31506 };
      studentClass.major = major;

      activatedRoute.data = of({ studentClass });
      comp.ngOnInit();

      expect(comp.majorsSharedCollection).toContain(major);
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
    describe('compareMajor', () => {
      it('Should forward to majorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(majorService, 'compareMajor');
        comp.compareMajor(entity, entity2);
        expect(majorService.compareMajor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
