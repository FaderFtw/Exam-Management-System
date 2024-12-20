import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITeachingSession } from '../teaching-session.model';

@Component({
  standalone: true,
  selector: 'jhi-teaching-session-detail',
  templateUrl: './teaching-session-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TeachingSessionDetailComponent {
  teachingSession = input<ITeachingSession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
