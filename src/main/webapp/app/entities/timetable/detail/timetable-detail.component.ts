import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITimetable } from '../timetable.model';

@Component({
  standalone: true,
  selector: 'jhi-timetable-detail',
  templateUrl: './timetable-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TimetableDetailComponent {
  timetable = input<ITimetable | null>(null);

  previousState(): void {
    window.history.back();
  }
}
