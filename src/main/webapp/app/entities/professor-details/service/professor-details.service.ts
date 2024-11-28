import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfessorDetails, NewProfessorDetails } from '../professor-details.model';

export type PartialUpdateProfessorDetails = Partial<IProfessorDetails> & Pick<IProfessorDetails, 'id'>;

export type EntityResponseType = HttpResponse<IProfessorDetails>;
export type EntityArrayResponseType = HttpResponse<IProfessorDetails[]>;

@Injectable({ providedIn: 'root' })
export class ProfessorDetailsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professor-details');

  create(professorDetails: NewProfessorDetails): Observable<EntityResponseType> {
    return this.http.post<IProfessorDetails>(this.resourceUrl, professorDetails, { observe: 'response' });
  }

  update(professorDetails: IProfessorDetails): Observable<EntityResponseType> {
    return this.http.put<IProfessorDetails>(
      `${this.resourceUrl}/${this.getProfessorDetailsIdentifier(professorDetails)}`,
      professorDetails,
      { observe: 'response' },
    );
  }

  partialUpdate(professorDetails: PartialUpdateProfessorDetails): Observable<EntityResponseType> {
    return this.http.patch<IProfessorDetails>(
      `${this.resourceUrl}/${this.getProfessorDetailsIdentifier(professorDetails)}`,
      professorDetails,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfessorDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfessorDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProfessorDetailsIdentifier(professorDetails: Pick<IProfessorDetails, 'id'>): number {
    return professorDetails.id;
  }

  compareProfessorDetails(o1: Pick<IProfessorDetails, 'id'> | null, o2: Pick<IProfessorDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfessorDetailsIdentifier(o1) === this.getProfessorDetailsIdentifier(o2) : o1 === o2;
  }

  addProfessorDetailsToCollectionIfMissing<Type extends Pick<IProfessorDetails, 'id'>>(
    professorDetailsCollection: Type[],
    ...professorDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const professorDetails: Type[] = professorDetailsToCheck.filter(isPresent);
    if (professorDetails.length > 0) {
      const professorDetailsCollectionIdentifiers = professorDetailsCollection.map(professorDetailsItem =>
        this.getProfessorDetailsIdentifier(professorDetailsItem),
      );
      const professorDetailsToAdd = professorDetails.filter(professorDetailsItem => {
        const professorDetailsIdentifier = this.getProfessorDetailsIdentifier(professorDetailsItem);
        if (professorDetailsCollectionIdentifiers.includes(professorDetailsIdentifier)) {
          return false;
        }
        professorDetailsCollectionIdentifiers.push(professorDetailsIdentifier);
        return true;
      });
      return [...professorDetailsToAdd, ...professorDetailsCollection];
    }
    return professorDetailsCollection;
  }
}
