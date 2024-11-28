import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { IExam } from '../exam.model';
import { ExamService } from '../service/exam.service';
import { ExamFormGroup, ExamFormService } from './exam-form.service';

@Component({
  standalone: true,
  selector: 'jhi-exam-update',
  templateUrl: './exam-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExamUpdateComponent implements OnInit {
  isSaving = false;
  exam: IExam | null = null;

  professorDetailsSharedCollection: IProfessorDetails[] = [];

  protected examService = inject(ExamService);
  protected examFormService = inject(ExamFormService);
  protected professorDetailsService = inject(ProfessorDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExamFormGroup = this.examFormService.createExamFormGroup();

  compareProfessorDetails = (o1: IProfessorDetails | null, o2: IProfessorDetails | null): boolean =>
    this.professorDetailsService.compareProfessorDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exam }) => {
      this.exam = exam;
      if (exam) {
        this.updateForm(exam);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exam = this.examFormService.getExam(this.editForm);
    if (exam.id !== null) {
      this.subscribeToSaveResponse(this.examService.update(exam));
    } else {
      this.subscribeToSaveResponse(this.examService.create(exam));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>): void {
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

  protected updateForm(exam: IExam): void {
    this.exam = exam;
    this.examFormService.resetForm(this.editForm, exam);

    this.professorDetailsSharedCollection = this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
      this.professorDetailsSharedCollection,
      ...(exam.supervisors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professorDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfessorDetails[]>) => res.body ?? []))
      .pipe(
        map((professorDetails: IProfessorDetails[]) =>
          this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
            professorDetails,
            ...(this.exam?.supervisors ?? []),
          ),
        ),
      )
      .subscribe((professorDetails: IProfessorDetails[]) => (this.professorDetailsSharedCollection = professorDetails));
  }
}
