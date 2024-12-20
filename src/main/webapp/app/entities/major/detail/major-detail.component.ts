import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMajor } from '../major.model';

@Component({
  standalone: true,
  selector: 'jhi-major-detail',
  templateUrl: './major-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MajorDetailComponent {
  major = input<IMajor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
