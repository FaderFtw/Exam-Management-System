import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMajor } from '../major.model';
import { MajorService } from '../service/major.service';

@Component({
  standalone: true,
  templateUrl: './major-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MajorDeleteDialogComponent {
  major?: IMajor;

  protected majorService = inject(MajorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.majorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
