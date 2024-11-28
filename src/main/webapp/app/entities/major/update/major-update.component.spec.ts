import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { StudentClassService } from 'app/entities/student-class/service/student-class.service';
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
  let studentClassService: StudentClassService;

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
    studentClassService = TestBed.inject(StudentClassService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StudentClass query and add missing value', () => {
      const major: IMajor = { id: 456 };
      const studentClass: IStudentClass = { id: 7153 };
      major.studentClass = studentClass;

      const studentClassCollection: IStudentClass[] = [{ id: 9714 }];
      jest.spyOn(studentClassService, 'query').mockReturnValue(of(new HttpResponse({ body: studentClassCollection })));
      const additionalStudentClasses = [studentClass];
      const expectedCollection: IStudentClass[] = [...additionalStudentClasses, ...studentClassCollection];
      jest.spyOn(studentClassService, 'addStudentClassToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ major });
      comp.ngOnInit();

      expect(studentClassService.query).toHaveBeenCalled();
      expect(studentClassService.addStudentClassToCollectionIfMissing).toHaveBeenCalledWith(
        studentClassCollection,
        ...additionalStudentClasses.map(expect.objectContaining),
      );
      expect(comp.studentClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const major: IMajor = { id: 456 };
      const studentClass: IStudentClass = { id: 14118 };
      major.studentClass = studentClass;

      activatedRoute.data = of({ major });
      comp.ngOnInit();

      expect(comp.studentClassesSharedCollection).toContain(studentClass);
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
    describe('compareStudentClass', () => {
      it('Should forward to studentClassService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(studentClassService, 'compareStudentClass');
        comp.compareStudentClass(entity, entity2);
        expect(studentClassService.compareStudentClass).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
