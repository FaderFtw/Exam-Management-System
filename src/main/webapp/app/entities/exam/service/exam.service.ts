import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExam, NewExam } from '../exam.model';

export type PartialUpdateExam = Partial<IExam> & Pick<IExam, 'id'>;

type RestOf<T extends IExam | NewExam> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestExam = RestOf<IExam>;

export type NewRestExam = RestOf<NewExam>;

export type PartialUpdateRestExam = RestOf<PartialUpdateExam>;

export type EntityResponseType = HttpResponse<IExam>;
export type EntityArrayResponseType = HttpResponse<IExam[]>;

@Injectable({ providedIn: 'root' })
export class ExamService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exams');

  create(exam: NewExam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exam);
    return this.http.post<RestExam>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(exam: IExam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exam);
    return this.http
      .put<RestExam>(`${this.resourceUrl}/${this.getExamIdentifier(exam)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(exam: PartialUpdateExam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exam);
    return this.http
      .patch<RestExam>(`${this.resourceUrl}/${this.getExamIdentifier(exam)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExamIdentifier(exam: Pick<IExam, 'id'>): number {
    return exam.id;
  }

  compareExam(o1: Pick<IExam, 'id'> | null, o2: Pick<IExam, 'id'> | null): boolean {
    return o1 && o2 ? this.getExamIdentifier(o1) === this.getExamIdentifier(o2) : o1 === o2;
  }

  addExamToCollectionIfMissing<Type extends Pick<IExam, 'id'>>(
    examCollection: Type[],
    ...examsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const exams: Type[] = examsToCheck.filter(isPresent);
    if (exams.length > 0) {
      const examCollectionIdentifiers = examCollection.map(examItem => this.getExamIdentifier(examItem));
      const examsToAdd = exams.filter(examItem => {
        const examIdentifier = this.getExamIdentifier(examItem);
        if (examCollectionIdentifiers.includes(examIdentifier)) {
          return false;
        }
        examCollectionIdentifiers.push(examIdentifier);
        return true;
      });
      return [...examsToAdd, ...examCollection];
    }
    return examCollection;
  }

  protected convertDateFromClient<T extends IExam | NewExam | PartialUpdateExam>(exam: T): RestOf<T> {
    return {
      ...exam,
      startTime: exam.startTime?.toJSON() ?? null,
      endTime: exam.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExam: RestExam): IExam {
    return {
      ...restExam,
      startTime: restExam.startTime ? dayjs(restExam.startTime) : undefined,
      endTime: restExam.endTime ? dayjs(restExam.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExam>): HttpResponse<IExam> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExam[]>): HttpResponse<IExam[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
