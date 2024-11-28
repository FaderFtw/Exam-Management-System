import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMajor, NewMajor } from '../major.model';

export type PartialUpdateMajor = Partial<IMajor> & Pick<IMajor, 'id'>;

export type EntityResponseType = HttpResponse<IMajor>;
export type EntityArrayResponseType = HttpResponse<IMajor[]>;

@Injectable({ providedIn: 'root' })
export class MajorService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/majors');

  create(major: NewMajor): Observable<EntityResponseType> {
    return this.http.post<IMajor>(this.resourceUrl, major, { observe: 'response' });
  }

  update(major: IMajor): Observable<EntityResponseType> {
    return this.http.put<IMajor>(`${this.resourceUrl}/${this.getMajorIdentifier(major)}`, major, { observe: 'response' });
  }

  partialUpdate(major: PartialUpdateMajor): Observable<EntityResponseType> {
    return this.http.patch<IMajor>(`${this.resourceUrl}/${this.getMajorIdentifier(major)}`, major, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMajor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMajor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMajorIdentifier(major: Pick<IMajor, 'id'>): number {
    return major.id;
  }

  compareMajor(o1: Pick<IMajor, 'id'> | null, o2: Pick<IMajor, 'id'> | null): boolean {
    return o1 && o2 ? this.getMajorIdentifier(o1) === this.getMajorIdentifier(o2) : o1 === o2;
  }

  addMajorToCollectionIfMissing<Type extends Pick<IMajor, 'id'>>(
    majorCollection: Type[],
    ...majorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const majors: Type[] = majorsToCheck.filter(isPresent);
    if (majors.length > 0) {
      const majorCollectionIdentifiers = majorCollection.map(majorItem => this.getMajorIdentifier(majorItem));
      const majorsToAdd = majors.filter(majorItem => {
        const majorIdentifier = this.getMajorIdentifier(majorItem);
        if (majorCollectionIdentifiers.includes(majorIdentifier)) {
          return false;
        }
        majorCollectionIdentifiers.push(majorIdentifier);
        return true;
      });
      return [...majorsToAdd, ...majorCollection];
    }
    return majorCollection;
  }
}
