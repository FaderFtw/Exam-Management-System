import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserAcademicInfo } from '../user-academic-info.model';

@Component({
  standalone: true,
  selector: 'jhi-user-academic-info-detail',
  templateUrl: './user-academic-info-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserAcademicInfoDetailComponent {
  userAcademicInfo = input<IUserAcademicInfo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
