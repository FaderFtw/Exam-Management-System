import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IStudentClass } from '../student-class.model';
import { StudentClassService } from '../service/student-class.service';

@Component({
  standalone: true,
  templateUrl: './student-class-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class StudentClassDeleteDialogComponent {
  studentClass?: IStudentClass;

  protected studentClassService = inject(StudentClassService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentClassService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
