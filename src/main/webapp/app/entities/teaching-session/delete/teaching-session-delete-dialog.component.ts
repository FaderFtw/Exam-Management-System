import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionService } from '../service/teaching-session.service';

@Component({
  standalone: true,
  templateUrl: './teaching-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TeachingSessionDeleteDialogComponent {
  teachingSession?: ITeachingSession;

  protected teachingSessionService = inject(TeachingSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teachingSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
