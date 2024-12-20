import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISessionType } from '../session-type.model';
import { SessionTypeService } from '../service/session-type.service';
import { SessionTypeFormGroup, SessionTypeFormService } from './session-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-session-type-update',
  templateUrl: './session-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SessionTypeUpdateComponent implements OnInit {
  isSaving = false;
  sessionType: ISessionType | null = null;

  protected sessionTypeService = inject(SessionTypeService);
  protected sessionTypeFormService = inject(SessionTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SessionTypeFormGroup = this.sessionTypeFormService.createSessionTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionType }) => {
      this.sessionType = sessionType;
      if (sessionType) {
        this.updateForm(sessionType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sessionType = this.sessionTypeFormService.getSessionType(this.editForm);
    if (sessionType.id !== null) {
      this.subscribeToSaveResponse(this.sessionTypeService.update(sessionType));
    } else {
      this.subscribeToSaveResponse(this.sessionTypeService.create(sessionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionType>>): void {
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

  protected updateForm(sessionType: ISessionType): void {
    this.sessionType = sessionType;
    this.sessionTypeFormService.resetForm(this.editForm, sessionType);
  }
}
