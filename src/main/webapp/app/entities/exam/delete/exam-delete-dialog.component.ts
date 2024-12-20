import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExam } from '../exam.model';
import { ExamService } from '../service/exam.service';

@Component({
  standalone: true,
  templateUrl: './exam-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExamDeleteDialogComponent {
  exam?: IExam;

  protected examService = inject(ExamService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.examService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
