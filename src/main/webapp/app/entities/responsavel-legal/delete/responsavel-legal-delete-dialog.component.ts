import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResponsavelLegal } from '../responsavel-legal.model';
import { ResponsavelLegalService } from '../service/responsavel-legal.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './responsavel-legal-delete-dialog.component.html',
})
export class ResponsavelLegalDeleteDialogComponent {
  responsavelLegal?: IResponsavelLegal;

  constructor(protected responsavelLegalService: ResponsavelLegalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.responsavelLegalService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
