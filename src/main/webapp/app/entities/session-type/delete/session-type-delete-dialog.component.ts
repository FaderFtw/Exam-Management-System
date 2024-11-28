import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISessionType } from '../session-type.model';
import { SessionTypeService } from '../service/session-type.service';

@Component({
  standalone: true,
  templateUrl: './session-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SessionTypeDeleteDialogComponent {
  sessionType?: ISessionType;

  protected sessionTypeService = inject(SessionTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
