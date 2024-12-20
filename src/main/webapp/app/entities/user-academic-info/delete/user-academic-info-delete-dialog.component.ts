import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserAcademicInfo } from '../user-academic-info.model';
import { UserAcademicInfoService } from '../service/user-academic-info.service';

@Component({
  standalone: true,
  templateUrl: './user-academic-info-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserAcademicInfoDeleteDialogComponent {
  userAcademicInfo?: IUserAcademicInfo;

  protected userAcademicInfoService = inject(UserAcademicInfoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAcademicInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
