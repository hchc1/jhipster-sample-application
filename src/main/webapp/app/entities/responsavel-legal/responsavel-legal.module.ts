import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResponsavelLegalComponent } from './list/responsavel-legal.component';
import { ResponsavelLegalDetailComponent } from './detail/responsavel-legal-detail.component';
import { ResponsavelLegalUpdateComponent } from './update/responsavel-legal-update.component';
import { ResponsavelLegalDeleteDialogComponent } from './delete/responsavel-legal-delete-dialog.component';
import { ResponsavelLegalRoutingModule } from './route/responsavel-legal-routing.module';

@NgModule({
  imports: [SharedModule, ResponsavelLegalRoutingModule],
  declarations: [
    ResponsavelLegalComponent,
    ResponsavelLegalDetailComponent,
    ResponsavelLegalUpdateComponent,
    ResponsavelLegalDeleteDialogComponent,
  ],
})
export class ResponsavelLegalModule {}
