import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstitute, NewInstitute } from '../institute.model';

export type PartialUpdateInstitute = Partial<IInstitute> & Pick<IInstitute, 'id'>;

export type EntityResponseType = HttpResponse<IInstitute>;
export type EntityArrayResponseType = HttpResponse<IInstitute[]>;

@Injectable({ providedIn: 'root' })
export class InstituteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/institutes');

  create(institute: NewInstitute): Observable<EntityResponseType> {
    return this.http.post<IInstitute>(this.resourceUrl, institute, { observe: 'response' });
  }

  update(institute: IInstitute): Observable<EntityResponseType> {
    return this.http.put<IInstitute>(`${this.resourceUrl}/${this.getInstituteIdentifier(institute)}`, institute, { observe: 'response' });
  }

  partialUpdate(institute: PartialUpdateInstitute): Observable<EntityResponseType> {
    return this.http.patch<IInstitute>(`${this.resourceUrl}/${this.getInstituteIdentifier(institute)}`, institute, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInstitute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstitute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInstituteIdentifier(institute: Pick<IInstitute, 'id'>): number {
    return institute.id;
  }

  compareInstitute(o1: Pick<IInstitute, 'id'> | null, o2: Pick<IInstitute, 'id'> | null): boolean {
    return o1 && o2 ? this.getInstituteIdentifier(o1) === this.getInstituteIdentifier(o2) : o1 === o2;
  }

  addInstituteToCollectionIfMissing<Type extends Pick<IInstitute, 'id'>>(
    instituteCollection: Type[],
    ...institutesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const institutes: Type[] = institutesToCheck.filter(isPresent);
    if (institutes.length > 0) {
      const instituteCollectionIdentifiers = instituteCollection.map(instituteItem => this.getInstituteIdentifier(instituteItem));
      const institutesToAdd = institutes.filter(instituteItem => {
        const instituteIdentifier = this.getInstituteIdentifier(instituteItem);
        if (instituteCollectionIdentifiers.includes(instituteIdentifier)) {
          return false;
        }
        instituteCollectionIdentifiers.push(instituteIdentifier);
        return true;
      });
      return [...institutesToAdd, ...instituteCollection];
    }
    return instituteCollection;
  }
}
