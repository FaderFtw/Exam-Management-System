import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClassroom, NewClassroom } from '../classroom.model';

export type PartialUpdateClassroom = Partial<IClassroom> & Pick<IClassroom, 'id'>;

export type EntityResponseType = HttpResponse<IClassroom>;
export type EntityArrayResponseType = HttpResponse<IClassroom[]>;

@Injectable({ providedIn: 'root' })
export class ClassroomService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/classrooms');

  create(classroom: NewClassroom): Observable<EntityResponseType> {
    return this.http.post<IClassroom>(this.resourceUrl, classroom, { observe: 'response' });
  }

  update(classroom: IClassroom): Observable<EntityResponseType> {
    return this.http.put<IClassroom>(`${this.resourceUrl}/${this.getClassroomIdentifier(classroom)}`, classroom, { observe: 'response' });
  }

  partialUpdate(classroom: PartialUpdateClassroom): Observable<EntityResponseType> {
    return this.http.patch<IClassroom>(`${this.resourceUrl}/${this.getClassroomIdentifier(classroom)}`, classroom, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassroom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassroom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClassroomIdentifier(classroom: Pick<IClassroom, 'id'>): number {
    return classroom.id;
  }

  compareClassroom(o1: Pick<IClassroom, 'id'> | null, o2: Pick<IClassroom, 'id'> | null): boolean {
    return o1 && o2 ? this.getClassroomIdentifier(o1) === this.getClassroomIdentifier(o2) : o1 === o2;
  }

  addClassroomToCollectionIfMissing<Type extends Pick<IClassroom, 'id'>>(
    classroomCollection: Type[],
    ...classroomsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const classrooms: Type[] = classroomsToCheck.filter(isPresent);
    if (classrooms.length > 0) {
      const classroomCollectionIdentifiers = classroomCollection.map(classroomItem => this.getClassroomIdentifier(classroomItem));
      const classroomsToAdd = classrooms.filter(classroomItem => {
        const classroomIdentifier = this.getClassroomIdentifier(classroomItem);
        if (classroomCollectionIdentifiers.includes(classroomIdentifier)) {
          return false;
        }
        classroomCollectionIdentifiers.push(classroomIdentifier);
        return true;
      });
      return [...classroomsToAdd, ...classroomCollection];
    }
    return classroomCollection;
  }
}
