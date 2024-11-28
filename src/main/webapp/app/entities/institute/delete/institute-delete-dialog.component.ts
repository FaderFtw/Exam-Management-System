import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInstitute } from '../institute.model';
import { InstituteService } from '../service/institute.service';

@Component({
  standalone: true,
  templateUrl: './institute-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InstituteDeleteDialogComponent {
  institute?: IInstitute;

  protected instituteService = inject(InstituteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instituteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
