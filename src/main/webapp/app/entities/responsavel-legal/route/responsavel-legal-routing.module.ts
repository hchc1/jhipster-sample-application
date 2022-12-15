import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResponsavelLegalComponent } from '../list/responsavel-legal.component';
import { ResponsavelLegalDetailComponent } from '../detail/responsavel-legal-detail.component';
import { ResponsavelLegalUpdateComponent } from '../update/responsavel-legal-update.component';
import { ResponsavelLegalRoutingResolveService } from './responsavel-legal-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const responsavelLegalRoute: Routes = [
  {
    path: '',
    component: ResponsavelLegalComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResponsavelLegalDetailComponent,
    resolve: {
      responsavelLegal: ResponsavelLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResponsavelLegalUpdateComponent,
    resolve: {
      responsavelLegal: ResponsavelLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResponsavelLegalUpdateComponent,
    resolve: {
      responsavelLegal: ResponsavelLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(responsavelLegalRoute)],
  exports: [RouterModule],
})
export class ResponsavelLegalRoutingModule {}
