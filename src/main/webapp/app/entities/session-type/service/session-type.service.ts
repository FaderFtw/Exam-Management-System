import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISessionType, NewSessionType } from '../session-type.model';

export type PartialUpdateSessionType = Partial<ISessionType> & Pick<ISessionType, 'id'>;

export type EntityResponseType = HttpResponse<ISessionType>;
export type EntityArrayResponseType = HttpResponse<ISessionType[]>;

@Injectable({ providedIn: 'root' })
export class SessionTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/session-types');

  create(sessionType: NewSessionType): Observable<EntityResponseType> {
    return this.http.post<ISessionType>(this.resourceUrl, sessionType, { observe: 'response' });
  }

  update(sessionType: ISessionType): Observable<EntityResponseType> {
    return this.http.put<ISessionType>(`${this.resourceUrl}/${this.getSessionTypeIdentifier(sessionType)}`, sessionType, {
      observe: 'response',
    });
  }

  partialUpdate(sessionType: PartialUpdateSessionType): Observable<EntityResponseType> {
    return this.http.patch<ISessionType>(`${this.resourceUrl}/${this.getSessionTypeIdentifier(sessionType)}`, sessionType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISessionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISessionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSessionTypeIdentifier(sessionType: Pick<ISessionType, 'id'>): number {
    return sessionType.id;
  }

  compareSessionType(o1: Pick<ISessionType, 'id'> | null, o2: Pick<ISessionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getSessionTypeIdentifier(o1) === this.getSessionTypeIdentifier(o2) : o1 === o2;
  }

  addSessionTypeToCollectionIfMissing<Type extends Pick<ISessionType, 'id'>>(
    sessionTypeCollection: Type[],
    ...sessionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sessionTypes: Type[] = sessionTypesToCheck.filter(isPresent);
    if (sessionTypes.length > 0) {
      const sessionTypeCollectionIdentifiers = sessionTypeCollection.map(sessionTypeItem => this.getSessionTypeIdentifier(sessionTypeItem));
      const sessionTypesToAdd = sessionTypes.filter(sessionTypeItem => {
        const sessionTypeIdentifier = this.getSessionTypeIdentifier(sessionTypeItem);
        if (sessionTypeCollectionIdentifiers.includes(sessionTypeIdentifier)) {
          return false;
        }
        sessionTypeCollectionIdentifiers.push(sessionTypeIdentifier);
        return true;
      });
      return [...sessionTypesToAdd, ...sessionTypeCollection];
    }
    return sessionTypeCollection;
  }
}
