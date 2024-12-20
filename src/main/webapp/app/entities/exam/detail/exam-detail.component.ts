import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IExam } from '../exam.model';

@Component({
  standalone: true,
  selector: 'jhi-exam-detail',
  templateUrl: './exam-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExamDetailComponent {
  exam = input<IExam | null>(null);

  previousState(): void {
    window.history.back();
  }
}
