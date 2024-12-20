import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { ProfessorDetailsService } from 'app/entities/professor-details/service/professor-details.service';
import { ITimetable } from '../timetable.model';
import { TimetableService } from '../service/timetable.service';
import { TimetableFormGroup, TimetableFormService } from './timetable-form.service';

@Component({
  standalone: true,
  selector: 'jhi-timetable-update',
  templateUrl: './timetable-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TimetableUpdateComponent implements OnInit {
  isSaving = false;
  timetable: ITimetable | null = null;

  professorDetailsSharedCollection: IProfessorDetails[] = [];

  protected timetableService = inject(TimetableService);
  protected timetableFormService = inject(TimetableFormService);
  protected professorDetailsService = inject(ProfessorDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TimetableFormGroup = this.timetableFormService.createTimetableFormGroup();

  compareProfessorDetails = (o1: IProfessorDetails | null, o2: IProfessorDetails | null): boolean =>
    this.professorDetailsService.compareProfessorDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timetable }) => {
      this.timetable = timetable;
      if (timetable) {
        this.updateForm(timetable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const timetable = this.timetableFormService.getTimetable(this.editForm);
    if (timetable.id !== null) {
      this.subscribeToSaveResponse(this.timetableService.update(timetable));
    } else {
      this.subscribeToSaveResponse(this.timetableService.create(timetable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimetable>>): void {
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

  protected updateForm(timetable: ITimetable): void {
    this.timetable = timetable;
    this.timetableFormService.resetForm(this.editForm, timetable);

    this.professorDetailsSharedCollection = this.professorDetailsService.addProfessorDetailsToCollectionIfMissing<IProfessorDetails>(
      this.professorDetailsSharedCollection,
      timetable.professor,
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
            this.timetable?.professor,
          ),
        ),
      )
      .subscribe((professorDetails: IProfessorDetails[]) => (this.professorDetailsSharedCollection = professorDetails));
  }
}
