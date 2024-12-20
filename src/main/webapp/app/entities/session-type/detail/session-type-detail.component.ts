import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISessionType } from '../session-type.model';

@Component({
  standalone: true,
  selector: 'jhi-session-type-detail',
  templateUrl: './session-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SessionTypeDetailComponent {
  sessionType = input<ISessionType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
