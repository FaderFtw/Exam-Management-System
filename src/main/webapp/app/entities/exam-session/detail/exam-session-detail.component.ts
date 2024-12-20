import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IExamSession } from '../exam-session.model';

@Component({
  standalone: true,
  selector: 'jhi-exam-session-detail',
  templateUrl: './exam-session-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExamSessionDetailComponent {
  examSession = input<IExamSession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
