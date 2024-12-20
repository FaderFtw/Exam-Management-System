import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { IUserAcademicInfo } from '../user-academic-info.model';
import { UserAcademicInfoService } from '../service/user-academic-info.service';
import { UserAcademicInfoFormService } from './user-academic-info-form.service';

import { UserAcademicInfoUpdateComponent } from './user-academic-info-update.component';

describe('UserAcademicInfo Management Update Component', () => {
  let comp: UserAcademicInfoUpdateComponent;
  let fixture: ComponentFixture<UserAcademicInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userAcademicInfoFormService: UserAcademicInfoFormService;
  let userAcademicInfoService: UserAcademicInfoService;
  let userService: UserService;
  let departmentService: DepartmentService;
  let instituteService: InstituteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserAcademicInfoUpdateComponent],
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
      .overrideTemplate(UserAcademicInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserAcademicInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userAcademicInfoFormService = TestBed.inject(UserAcademicInfoFormService);
    userAcademicInfoService = TestBed.inject(UserAcademicInfoService);
    userService = TestBed.inject(UserService);
    departmentService = TestBed.inject(DepartmentService);
    instituteService = TestBed.inject(InstituteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userAcademicInfo: IUserAcademicInfo = { id: 456 };
      const user: IUser = { id: 3635 };
      userAcademicInfo.user = user;

      const userCollection: IUser[] = [{ id: 1638 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const userAcademicInfo: IUserAcademicInfo = { id: 456 };
      const department: IDepartment = { id: 2152 };
      userAcademicInfo.department = department;

      const departmentCollection: IDepartment[] = [{ id: 14916 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining),
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Institute query and add missing value', () => {
      const userAcademicInfo: IUserAcademicInfo = { id: 456 };
      const institute: IInstitute = { id: 7747 };
      userAcademicInfo.institute = institute;

      const instituteCollection: IInstitute[] = [{ id: 14914 }];
      jest.spyOn(instituteService, 'query').mockReturnValue(of(new HttpResponse({ body: instituteCollection })));
      const additionalInstitutes = [institute];
      const expectedCollection: IInstitute[] = [...additionalInstitutes, ...instituteCollection];
      jest.spyOn(instituteService, 'addInstituteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      expect(instituteService.query).toHaveBeenCalled();
      expect(instituteService.addInstituteToCollectionIfMissing).toHaveBeenCalledWith(
        instituteCollection,
        ...additionalInstitutes.map(expect.objectContaining),
      );
      expect(comp.institutesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userAcademicInfo: IUserAcademicInfo = { id: 456 };
      const user: IUser = { id: 6466 };
      userAcademicInfo.user = user;
      const department: IDepartment = { id: 4944 };
      userAcademicInfo.department = department;
      const institute: IInstitute = { id: 29582 };
      userAcademicInfo.institute = institute;

      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.institutesSharedCollection).toContain(institute);
      expect(comp.userAcademicInfo).toEqual(userAcademicInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAcademicInfo>>();
      const userAcademicInfo = { id: 123 };
      jest.spyOn(userAcademicInfoFormService, 'getUserAcademicInfo').mockReturnValue(userAcademicInfo);
      jest.spyOn(userAcademicInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAcademicInfo }));
      saveSubject.complete();

      // THEN
      expect(userAcademicInfoFormService.getUserAcademicInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userAcademicInfoService.update).toHaveBeenCalledWith(expect.objectContaining(userAcademicInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAcademicInfo>>();
      const userAcademicInfo = { id: 123 };
      jest.spyOn(userAcademicInfoFormService, 'getUserAcademicInfo').mockReturnValue({ id: null });
      jest.spyOn(userAcademicInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAcademicInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userAcademicInfo }));
      saveSubject.complete();

      // THEN
      expect(userAcademicInfoFormService.getUserAcademicInfo).toHaveBeenCalled();
      expect(userAcademicInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserAcademicInfo>>();
      const userAcademicInfo = { id: 123 };
      jest.spyOn(userAcademicInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userAcademicInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userAcademicInfoService.update).toHaveBeenCalled();
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

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInstitute', () => {
      it('Should forward to instituteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(instituteService, 'compareInstitute');
        comp.compareInstitute(entity, entity2);
        expect(instituteService.compareInstitute).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
