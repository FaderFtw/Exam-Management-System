import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITimetable } from '../timetable.model';
import { TimetableService } from '../service/timetable.service';

@Component({
  standalone: true,
  templateUrl: './timetable-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TimetableDeleteDialogComponent {
  timetable?: ITimetable;

  protected timetableService = inject(TimetableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timetableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
