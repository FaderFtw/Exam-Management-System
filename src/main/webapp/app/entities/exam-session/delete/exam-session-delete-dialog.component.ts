import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExamSession } from '../exam-session.model';
import { ExamSessionService } from '../service/exam-session.service';

@Component({
  standalone: true,
  templateUrl: './exam-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExamSessionDeleteDialogComponent {
  examSession?: IExamSession;

  protected examSessionService = inject(ExamSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.examSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
