import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IStudentClass } from '../student-class.model';

@Component({
  standalone: true,
  selector: 'jhi-student-class-detail',
  templateUrl: './student-class-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class StudentClassDetailComponent {
  studentClass = input<IStudentClass | null>(null);

  previousState(): void {
    window.history.back();
  }
}
