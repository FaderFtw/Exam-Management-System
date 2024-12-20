import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudentClass, NewStudentClass } from '../student-class.model';

export type PartialUpdateStudentClass = Partial<IStudentClass> & Pick<IStudentClass, 'id'>;

export type EntityResponseType = HttpResponse<IStudentClass>;
export type EntityArrayResponseType = HttpResponse<IStudentClass[]>;

@Injectable({ providedIn: 'root' })
export class StudentClassService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/student-classes');

  create(studentClass: NewStudentClass): Observable<EntityResponseType> {
    return this.http.post<IStudentClass>(this.resourceUrl, studentClass, { observe: 'response' });
  }

  update(studentClass: IStudentClass): Observable<EntityResponseType> {
    return this.http.put<IStudentClass>(`${this.resourceUrl}/${this.getStudentClassIdentifier(studentClass)}`, studentClass, {
      observe: 'response',
    });
  }

  partialUpdate(studentClass: PartialUpdateStudentClass): Observable<EntityResponseType> {
    return this.http.patch<IStudentClass>(`${this.resourceUrl}/${this.getStudentClassIdentifier(studentClass)}`, studentClass, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStudentClass>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStudentClass[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStudentClassIdentifier(studentClass: Pick<IStudentClass, 'id'>): number {
    return studentClass.id;
  }

  compareStudentClass(o1: Pick<IStudentClass, 'id'> | null, o2: Pick<IStudentClass, 'id'> | null): boolean {
    return o1 && o2 ? this.getStudentClassIdentifier(o1) === this.getStudentClassIdentifier(o2) : o1 === o2;
  }

  addStudentClassToCollectionIfMissing<Type extends Pick<IStudentClass, 'id'>>(
    studentClassCollection: Type[],
    ...studentClassesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const studentClasses: Type[] = studentClassesToCheck.filter(isPresent);
    if (studentClasses.length > 0) {
      const studentClassCollectionIdentifiers = studentClassCollection.map(studentClassItem =>
        this.getStudentClassIdentifier(studentClassItem),
      );
      const studentClassesToAdd = studentClasses.filter(studentClassItem => {
        const studentClassIdentifier = this.getStudentClassIdentifier(studentClassItem);
        if (studentClassCollectionIdentifiers.includes(studentClassIdentifier)) {
          return false;
        }
        studentClassCollectionIdentifiers.push(studentClassIdentifier);
        return true;
      });
      return [...studentClassesToAdd, ...studentClassCollection];
    }
    return studentClassCollection;
  }
}
