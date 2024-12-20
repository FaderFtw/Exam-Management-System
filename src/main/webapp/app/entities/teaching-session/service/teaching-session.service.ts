import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeachingSession, NewTeachingSession } from '../teaching-session.model';

export type PartialUpdateTeachingSession = Partial<ITeachingSession> & Pick<ITeachingSession, 'id'>;

type RestOf<T extends ITeachingSession | NewTeachingSession> = Omit<T, 'startHour' | 'endHour'> & {
  startHour?: string | null;
  endHour?: string | null;
};

export type RestTeachingSession = RestOf<ITeachingSession>;

export type NewRestTeachingSession = RestOf<NewTeachingSession>;

export type PartialUpdateRestTeachingSession = RestOf<PartialUpdateTeachingSession>;

export type EntityResponseType = HttpResponse<ITeachingSession>;
export type EntityArrayResponseType = HttpResponse<ITeachingSession[]>;

@Injectable({ providedIn: 'root' })
export class TeachingSessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/teaching-sessions');

  create(teachingSession: NewTeachingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(teachingSession);
    return this.http
      .post<RestTeachingSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(teachingSession: ITeachingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(teachingSession);
    return this.http
      .put<RestTeachingSession>(`${this.resourceUrl}/${this.getTeachingSessionIdentifier(teachingSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(teachingSession: PartialUpdateTeachingSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(teachingSession);
    return this.http
      .patch<RestTeachingSession>(`${this.resourceUrl}/${this.getTeachingSessionIdentifier(teachingSession)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTeachingSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTeachingSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTeachingSessionIdentifier(teachingSession: Pick<ITeachingSession, 'id'>): number {
    return teachingSession.id;
  }

  compareTeachingSession(o1: Pick<ITeachingSession, 'id'> | null, o2: Pick<ITeachingSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getTeachingSessionIdentifier(o1) === this.getTeachingSessionIdentifier(o2) : o1 === o2;
  }

  addTeachingSessionToCollectionIfMissing<Type extends Pick<ITeachingSession, 'id'>>(
    teachingSessionCollection: Type[],
    ...teachingSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const teachingSessions: Type[] = teachingSessionsToCheck.filter(isPresent);
    if (teachingSessions.length > 0) {
      const teachingSessionCollectionIdentifiers = teachingSessionCollection.map(teachingSessionItem =>
        this.getTeachingSessionIdentifier(teachingSessionItem),
      );
      const teachingSessionsToAdd = teachingSessions.filter(teachingSessionItem => {
        const teachingSessionIdentifier = this.getTeachingSessionIdentifier(teachingSessionItem);
        if (teachingSessionCollectionIdentifiers.includes(teachingSessionIdentifier)) {
          return false;
        }
        teachingSessionCollectionIdentifiers.push(teachingSessionIdentifier);
        return true;
      });
      return [...teachingSessionsToAdd, ...teachingSessionCollection];
    }
    return teachingSessionCollection;
  }

  protected convertDateFromClient<T extends ITeachingSession | NewTeachingSession | PartialUpdateTeachingSession>(
    teachingSession: T,
  ): RestOf<T> {
    return {
      ...teachingSession,
      startHour: teachingSession.startHour?.toJSON() ?? null,
      endHour: teachingSession.endHour?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTeachingSession: RestTeachingSession): ITeachingSession {
    return {
      ...restTeachingSession,
      startHour: restTeachingSession.startHour ? dayjs(restTeachingSession.startHour) : undefined,
      endHour: restTeachingSession.endHour ? dayjs(restTeachingSession.endHour) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTeachingSession>): HttpResponse<ITeachingSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTeachingSession[]>): HttpResponse<ITeachingSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
