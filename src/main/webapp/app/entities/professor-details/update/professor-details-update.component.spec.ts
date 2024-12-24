import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { IProfessorDetails } from '../professor-details.model';
import { ProfessorDetailsService } from '../service/professor-details.service';
import { ProfessorDetailsFormService } from './professor-details-form.service';

import { ProfessorDetailsUpdateComponent } from './professor-details-update.component';

describe('ProfessorDetails Management Update Component', () => {
  let comp: ProfessorDetailsUpdateComponent;
  let fixture: ComponentFixture<ProfessorDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let professorDetailsFormService: ProfessorDetailsFormService;
  let professorDetailsService: ProfessorDetailsService;
  let userService: UserService;
  let examService: ExamService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfessorDetailsUpdateComponent],
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
      .overrideTemplate(ProfessorDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfessorDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    professorDetailsFormService = TestBed.inject(ProfessorDetailsFormService);
    professorDetailsService = TestBed.inject(ProfessorDetailsService);
    userService = TestBed.inject(UserService);
    examService = TestBed.inject(ExamService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const professorDetails: IProfessorDetails = { id: 456 };
      const user: IUser = { id: 19134 };
      professorDetails.user = user;

      const userCollection: IUser[] = [{ id: 13585 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ professorDetails });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Exam query and add missing value', () => {
      const professorDetails: IProfessorDetails = { id: 456 };
      const supervisedExams: IExam[] = [{ id: 25177 }];
      professorDetails.supervisedExams = supervisedExams;

      const examCollection: IExam[] = [{ id: 11525 }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [...supervisedExams];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ professorDetails });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const professorDetails: IProfessorDetails = { id: 456 };
      const user: IUser = { id: 18803 };
      professorDetails.user = user;
      const supervisedExams: IExam = { id: 7825 };
      professorDetails.supervisedExams = [supervisedExams];

      activatedRoute.data = of({ professorDetails });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.examsSharedCollection).toContain(supervisedExams);
      expect(comp.professorDetails).toEqual(professorDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessorDetails>>();
      const professorDetails = { id: 123 };
      jest.spyOn(professorDetailsFormService, 'getProfessorDetails').mockReturnValue(professorDetails);
      jest.spyOn(professorDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professorDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professorDetails }));
      saveSubject.complete();

      // THEN
      expect(professorDetailsFormService.getProfessorDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(professorDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(professorDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessorDetails>>();
      const professorDetails = { id: 123 };
      jest.spyOn(professorDetailsFormService, 'getProfessorDetails').mockReturnValue({ id: null });
      jest.spyOn(professorDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professorDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: professorDetails }));
      saveSubject.complete();

      // THEN
      expect(professorDetailsFormService.getProfessorDetails).toHaveBeenCalled();
      expect(professorDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfessorDetails>>();
      const professorDetails = { id: 123 };
      jest.spyOn(professorDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ professorDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(professorDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExam', () => {
      it('Should forward to examService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(examService, 'compareExam');
        comp.compareExam(entity, entity2);
        expect(examService.compareExam).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
