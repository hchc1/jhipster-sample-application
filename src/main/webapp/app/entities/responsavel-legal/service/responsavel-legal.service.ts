import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResponsavelLegal, NewResponsavelLegal } from '../responsavel-legal.model';

export type PartialUpdateResponsavelLegal = Partial<IResponsavelLegal> & Pick<IResponsavelLegal, 'id'>;

export type EntityResponseType = HttpResponse<IResponsavelLegal>;
export type EntityArrayResponseType = HttpResponse<IResponsavelLegal[]>;

@Injectable({ providedIn: 'root' })
export class ResponsavelLegalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/responsavel-legals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(responsavelLegal: NewResponsavelLegal): Observable<EntityResponseType> {
    return this.http.post<IResponsavelLegal>(this.resourceUrl, responsavelLegal, { observe: 'response' });
  }

  update(responsavelLegal: IResponsavelLegal): Observable<EntityResponseType> {
    return this.http.put<IResponsavelLegal>(
      `${this.resourceUrl}/${this.getResponsavelLegalIdentifier(responsavelLegal)}`,
      responsavelLegal,
      { observe: 'response' }
    );
  }

  partialUpdate(responsavelLegal: PartialUpdateResponsavelLegal): Observable<EntityResponseType> {
    return this.http.patch<IResponsavelLegal>(
      `${this.resourceUrl}/${this.getResponsavelLegalIdentifier(responsavelLegal)}`,
      responsavelLegal,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResponsavelLegal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResponsavelLegal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResponsavelLegalIdentifier(responsavelLegal: Pick<IResponsavelLegal, 'id'>): number {
    return responsavelLegal.id;
  }

  compareResponsavelLegal(o1: Pick<IResponsavelLegal, 'id'> | null, o2: Pick<IResponsavelLegal, 'id'> | null): boolean {
    return o1 && o2 ? this.getResponsavelLegalIdentifier(o1) === this.getResponsavelLegalIdentifier(o2) : o1 === o2;
  }

  addResponsavelLegalToCollectionIfMissing<Type extends Pick<IResponsavelLegal, 'id'>>(
    responsavelLegalCollection: Type[],
    ...responsavelLegalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const responsavelLegals: Type[] = responsavelLegalsToCheck.filter(isPresent);
    if (responsavelLegals.length > 0) {
      const responsavelLegalCollectionIdentifiers = responsavelLegalCollection.map(
        responsavelLegalItem => this.getResponsavelLegalIdentifier(responsavelLegalItem)!
      );
      const responsavelLegalsToAdd = responsavelLegals.filter(responsavelLegalItem => {
        const responsavelLegalIdentifier = this.getResponsavelLegalIdentifier(responsavelLegalItem);
        if (responsavelLegalCollectionIdentifiers.includes(responsavelLegalIdentifier)) {
          return false;
        }
        responsavelLegalCollectionIdentifiers.push(responsavelLegalIdentifier);
        return true;
      });
      return [...responsavelLegalsToAdd, ...responsavelLegalCollection];
    }
    return responsavelLegalCollection;
  }
}
