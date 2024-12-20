import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExamSession, NewExamSession } from '../exam-session.model';

export type PartialUpdateExamSession = Partial<IExamSession> & Pick<IExamSession, 'id'>;

type RestOf<T extends IExamSession | NewExamSession> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestExamSession = RestOf<IExamSession>;

export type NewRestExamSession = RestOf<NewExamSession>;

export type PartialUpdateRestExamSession = RestOf<PartialUpdateExamSession>;

export type EntityResponseType = HttpResponse<IExamSession>;
export type EntityArrayResponseType = HttpResponse<IExamSession[]>;

@Injectable({ providedIn: 'root' })
export class ExamSessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exam-sessions');

  create(examSession: NewExamSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examSession);
    return this.http
      .post<RestExamSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(examSession: IExamSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examSession);
    return this.http
      .put<RestExamSession>(`${this.resourceUrl}/${this.getExamSessionIdentifier(examSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(examSession: PartialUpdateExamSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examSession);
    return this.http
      .patch<RestExamSession>(`${this.resourceUrl}/${this.getExamSessionIdentifier(examSession)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExamSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExamSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExamSessionIdentifier(examSession: Pick<IExamSession, 'id'>): number {
    return examSession.id;
  }

  compareExamSession(o1: Pick<IExamSession, 'id'> | null, o2: Pick<IExamSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getExamSessionIdentifier(o1) === this.getExamSessionIdentifier(o2) : o1 === o2;
  }

  addExamSessionToCollectionIfMissing<Type extends Pick<IExamSession, 'id'>>(
    examSessionCollection: Type[],
    ...examSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const examSessions: Type[] = examSessionsToCheck.filter(isPresent);
    if (examSessions.length > 0) {
      const examSessionCollectionIdentifiers = examSessionCollection.map(examSessionItem => this.getExamSessionIdentifier(examSessionItem));
      const examSessionsToAdd = examSessions.filter(examSessionItem => {
        const examSessionIdentifier = this.getExamSessionIdentifier(examSessionItem);
        if (examSessionCollectionIdentifiers.includes(examSessionIdentifier)) {
          return false;
        }
        examSessionCollectionIdentifiers.push(examSessionIdentifier);
        return true;
      });
      return [...examSessionsToAdd, ...examSessionCollection];
    }
    return examSessionCollection;
  }

  protected convertDateFromClient<T extends IExamSession | NewExamSession | PartialUpdateExamSession>(examSession: T): RestOf<T> {
    return {
      ...examSession,
      startDate: examSession.startDate?.format(DATE_FORMAT) ?? null,
      endDate: examSession.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restExamSession: RestExamSession): IExamSession {
    return {
      ...restExamSession,
      startDate: restExamSession.startDate ? dayjs(restExamSession.startDate) : undefined,
      endDate: restExamSession.endDate ? dayjs(restExamSession.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExamSession>): HttpResponse<IExamSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExamSession[]>): HttpResponse<IExamSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
