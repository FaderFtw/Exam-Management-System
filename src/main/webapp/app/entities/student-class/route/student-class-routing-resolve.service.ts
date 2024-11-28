import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudentClass } from '../student-class.model';
import { StudentClassService } from '../service/student-class.service';

const studentClassResolve = (route: ActivatedRouteSnapshot): Observable<null | IStudentClass> => {
  const id = route.params.id;
  if (id) {
    return inject(StudentClassService)
      .find(id)
      .pipe(
        mergeMap((studentClass: HttpResponse<IStudentClass>) => {
          if (studentClass.body) {
            return of(studentClass.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default studentClassResolve;
