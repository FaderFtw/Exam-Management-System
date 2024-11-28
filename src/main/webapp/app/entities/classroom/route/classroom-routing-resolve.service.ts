import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassroom } from '../classroom.model';
import { ClassroomService } from '../service/classroom.service';

const classroomResolve = (route: ActivatedRouteSnapshot): Observable<null | IClassroom> => {
  const id = route.params.id;
  if (id) {
    return inject(ClassroomService)
      .find(id)
      .pipe(
        mergeMap((classroom: HttpResponse<IClassroom>) => {
          if (classroom.body) {
            return of(classroom.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default classroomResolve;
