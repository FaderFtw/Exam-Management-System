import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExam, NewExam } from '../exam.model';

export type PartialUpdateExam = Partial<IExam> & Pick<IExam, 'id'>;

export type EntityResponseType = HttpResponse<IExam>;
export type EntityArrayResponseType = HttpResponse<IExam[]>;

@Injectable({ providedIn: 'root' })
export class ExamService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exams');

  create(exam: NewExam): Observable<EntityResponseType> {
    return this.http.post<IExam>(this.resourceUrl, exam, { observe: 'response' });
  }

  update(exam: IExam): Observable<EntityResponseType> {
    return this.http.put<IExam>(`${this.resourceUrl}/${this.getExamIdentifier(exam)}`, exam, { observe: 'response' });
  }

  partialUpdate(exam: PartialUpdateExam): Observable<EntityResponseType> {
    return this.http.patch<IExam>(`${this.resourceUrl}/${this.getExamIdentifier(exam)}`, exam, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExam[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
