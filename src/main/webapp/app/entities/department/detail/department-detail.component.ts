import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDepartment } from '../department.model';

@Component({
  standalone: true,
  selector: 'jhi-department-detail',
  templateUrl: './department-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DepartmentDetailComponent {
  department = input<IDepartment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
