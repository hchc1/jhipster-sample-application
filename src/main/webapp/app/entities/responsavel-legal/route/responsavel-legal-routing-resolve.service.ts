import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResponsavelLegal } from '../responsavel-legal.model';
import { ResponsavelLegalService } from '../service/responsavel-legal.service';

@Injectable({ providedIn: 'root' })
export class ResponsavelLegalRoutingResolveService implements Resolve<IResponsavelLegal | null> {
  constructor(protected service: ResponsavelLegalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResponsavelLegal | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((responsavelLegal: HttpResponse<IResponsavelLegal>) => {
          if (responsavelLegal.body) {
            return of(responsavelLegal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
