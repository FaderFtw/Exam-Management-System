import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { Rank } from 'app/entities/enumerations/rank.model';
import { ProfessorDetailsService } from '../service/professor-details.service';
import { IProfessorDetails } from '../professor-details.model';
import { ProfessorDetailsFormGroup, ProfessorDetailsFormService } from './professor-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-professor-details-update',
  templateUrl: './professor-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessorDetailsUpdateComponent implements OnInit {
  isSaving = false;
  professorDetails: IProfessorDetails | null = null;
  rankValues = Object.keys(Rank);

  usersSharedCollection: IUser[] = [];
  examsSharedCollection: IExam[] = [];

  protected professorDetailsService = inject(ProfessorDetailsService);
  protected professorDetailsFormService = inject(ProfessorDetailsFormService);
  protected userService = inject(UserService);
  protected examService = inject(ExamService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfessorDetailsFormGroup = this.professorDetailsFormService.createProfessorDetailsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professorDetails }) => {
      this.professorDetails = professorDetails;
      if (professorDetails) {
        this.updateForm(professorDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professorDetails = this.professorDetailsFormService.getProfessorDetails(this.editForm);
    if (professorDetails.id !== null) {
      this.subscribeToSaveResponse(this.professorDetailsService.update(professorDetails));
    } else {
      this.subscribeToSaveResponse(this.professorDetailsService.create(professorDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessorDetails>>): void {
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

  protected updateForm(professorDetails: IProfessorDetails): void {
    this.professorDetails = professorDetails;
    this.professorDetailsFormService.resetForm(this.editForm, professorDetails);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, professorDetails.user);
    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(
      this.examsSharedCollection,
      ...(professorDetails.supervisedExams ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.professorDetails?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(
        map((exams: IExam[]) =>
          this.examService.addExamToCollectionIfMissing<IExam>(exams, ...(this.professorDetails?.supervisedExams ?? [])),
        ),
      )
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));
  }
}
