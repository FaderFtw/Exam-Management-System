import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProfessorDetails } from '../professor-details.model';

@Component({
  standalone: true,
  selector: 'jhi-professor-details-detail',
  templateUrl: './professor-details-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProfessorDetailsDetailComponent {
  professorDetails = input<IProfessorDetails | null>(null);

  previousState(): void {
    window.history.back();
  }
}
