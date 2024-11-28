import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProfessorDetails } from '../professor-details.model';
import { ProfessorDetailsService } from '../service/professor-details.service';

@Component({
  standalone: true,
  templateUrl: './professor-details-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProfessorDetailsDeleteDialogComponent {
  professorDetails?: IProfessorDetails;

  protected professorDetailsService = inject(ProfessorDetailsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.professorDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
