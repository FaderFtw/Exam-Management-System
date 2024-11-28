import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { ExamService } from '../service/exam.service';
import { IExam } from '../exam.model';
import { ExamFormService } from './exam-form.service';

import { ExamUpdateComponent } from './exam-update.component';

describe('Exam Management Update Component', () => {
  let comp: ExamUpdateComponent;
  let fixture: ComponentFixture<ExamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let examFormService: ExamFormService;
  let examService: ExamService;
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
    professorDetailsService = TestBed.inject(ProfessorDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProfessorDetails query and add missing value', () => {
      const exam: IExam = { id: 456 };
      const supervisors: IProfessorDetails[] = [{ id: 22530 }];
      exam.supervisors = supervisors;

      const professorDetailsCollection: IProfessorDetails[] = [{ id: 29628 }];
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
      const supervisors: IProfessorDetails = { id: 14025 };
      exam.supervisors = [supervisors];

      activatedRoute.data = of({ exam });
      comp.ngOnInit();

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
