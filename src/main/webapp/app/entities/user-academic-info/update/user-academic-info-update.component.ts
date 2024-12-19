import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IInstitute } from 'app/entities/institute/institute.model';
import { InstituteService } from 'app/entities/institute/service/institute.service';
import { UserAcademicInfoService } from '../service/user-academic-info.service';
import { IUserAcademicInfo } from '../user-academic-info.model';
import { UserAcademicInfoFormGroup, UserAcademicInfoFormService } from './user-academic-info-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-academic-info-update',
  templateUrl: './user-academic-info-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserAcademicInfoUpdateComponent implements OnInit {
  isSaving = false;
  userAcademicInfo: IUserAcademicInfo | null = null;

  usersSharedCollection: IUser[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  institutesSharedCollection: IInstitute[] = [];

  protected userAcademicInfoService = inject(UserAcademicInfoService);
  protected userAcademicInfoFormService = inject(UserAcademicInfoFormService);
  protected userService = inject(UserService);
  protected departmentService = inject(DepartmentService);
  protected instituteService = inject(InstituteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserAcademicInfoFormGroup = this.userAcademicInfoFormService.createUserAcademicInfoFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareInstitute = (o1: IInstitute | null, o2: IInstitute | null): boolean => this.instituteService.compareInstitute(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAcademicInfo }) => {
      this.userAcademicInfo = userAcademicInfo;
      if (userAcademicInfo) {
        this.updateForm(userAcademicInfo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAcademicInfo = this.userAcademicInfoFormService.getUserAcademicInfo(this.editForm);
    if (userAcademicInfo.id !== null) {
      this.subscribeToSaveResponse(this.userAcademicInfoService.update(userAcademicInfo));
    } else {
      this.subscribeToSaveResponse(this.userAcademicInfoService.create(userAcademicInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAcademicInfo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userAcademicInfo: IUserAcademicInfo): void {
    this.userAcademicInfo = userAcademicInfo;
    this.userAcademicInfoFormService.resetForm(this.editForm, userAcademicInfo);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userAcademicInfo.user);
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      userAcademicInfo.department,
    );
    this.institutesSharedCollection = this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(
      this.institutesSharedCollection,
      userAcademicInfo.institute,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userAcademicInfo?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.userAcademicInfo?.department),
        ),
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.instituteService
      .query()
      .pipe(map((res: HttpResponse<IInstitute[]>) => res.body ?? []))
      .pipe(
        map((institutes: IInstitute[]) =>
          this.instituteService.addInstituteToCollectionIfMissing<IInstitute>(institutes, this.userAcademicInfo?.institute),
        ),
      )
      .subscribe((institutes: IInstitute[]) => (this.institutesSharedCollection = institutes));
  }
}
