import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITimetable, NewTimetable } from '../timetable.model';

export type PartialUpdateTimetable = Partial<ITimetable> & Pick<ITimetable, 'id'>;

type RestOf<T extends ITimetable | NewTimetable> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestTimetable = RestOf<ITimetable>;

export type NewRestTimetable = RestOf<NewTimetable>;

export type PartialUpdateRestTimetable = RestOf<PartialUpdateTimetable>;

export type EntityResponseType = HttpResponse<ITimetable>;
export type EntityArrayResponseType = HttpResponse<ITimetable[]>;

@Injectable({ providedIn: 'root' })
export class TimetableService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/timetables');

  create(timetable: NewTimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timetable);
    return this.http
      .post<RestTimetable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(timetable: ITimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timetable);
    return this.http
      .put<RestTimetable>(`${this.resourceUrl}/${this.getTimetableIdentifier(timetable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(timetable: PartialUpdateTimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timetable);
    return this.http
      .patch<RestTimetable>(`${this.resourceUrl}/${this.getTimetableIdentifier(timetable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTimetable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTimetable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTimetableIdentifier(timetable: Pick<ITimetable, 'id'>): number {
    return timetable.id;
  }

  compareTimetable(o1: Pick<ITimetable, 'id'> | null, o2: Pick<ITimetable, 'id'> | null): boolean {
    return o1 && o2 ? this.getTimetableIdentifier(o1) === this.getTimetableIdentifier(o2) : o1 === o2;
  }

  addTimetableToCollectionIfMissing<Type extends Pick<ITimetable, 'id'>>(
    timetableCollection: Type[],
    ...timetablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const timetables: Type[] = timetablesToCheck.filter(isPresent);
    if (timetables.length > 0) {
      const timetableCollectionIdentifiers = timetableCollection.map(timetableItem => this.getTimetableIdentifier(timetableItem));
      const timetablesToAdd = timetables.filter(timetableItem => {
        const timetableIdentifier = this.getTimetableIdentifier(timetableItem);
        if (timetableCollectionIdentifiers.includes(timetableIdentifier)) {
          return false;
        }
        timetableCollectionIdentifiers.push(timetableIdentifier);
        return true;
      });
      return [...timetablesToAdd, ...timetableCollection];
    }
    return timetableCollection;
  }

  protected convertDateFromClient<T extends ITimetable | NewTimetable | PartialUpdateTimetable>(timetable: T): RestOf<T> {
    return {
      ...timetable,
      startDate: timetable.startDate?.format(DATE_FORMAT) ?? null,
      endDate: timetable.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTimetable: RestTimetable): ITimetable {
    return {
      ...restTimetable,
      startDate: restTimetable.startDate ? dayjs(restTimetable.startDate) : undefined,
      endDate: restTimetable.endDate ? dayjs(restTimetable.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTimetable>): HttpResponse<ITimetable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTimetable[]>): HttpResponse<ITimetable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
