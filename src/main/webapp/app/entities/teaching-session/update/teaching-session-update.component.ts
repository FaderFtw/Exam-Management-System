import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionService } from '../service/teaching-session.service';
import { TeachingSessionFormGroup, TeachingSessionFormService } from './teaching-session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-teaching-session-update',
  templateUrl: './teaching-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeachingSessionUpdateComponent implements OnInit {
  isSaving = false;
  teachingSession: ITeachingSession | null = null;

  protected teachingSessionService = inject(TeachingSessionService);
  protected teachingSessionFormService = inject(TeachingSessionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeachingSessionFormGroup = this.teachingSessionFormService.createTeachingSessionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teachingSession }) => {
      this.teachingSession = teachingSession;
      if (teachingSession) {
        this.updateForm(teachingSession);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teachingSession = this.teachingSessionFormService.getTeachingSession(this.editForm);
    if (teachingSession.id !== null) {
      this.subscribeToSaveResponse(this.teachingSessionService.update(teachingSession));
    } else {
      this.subscribeToSaveResponse(this.teachingSessionService.create(teachingSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeachingSession>>): void {
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

  protected updateForm(teachingSession: ITeachingSession): void {
    this.teachingSession = teachingSession;
    this.teachingSessionFormService.resetForm(this.editForm, teachingSession);
  }
}
