import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExam } from '../exam.model';
import { ExamService } from '../service/exam.service';

const examResolve = (route: ActivatedRouteSnapshot): Observable<null | IExam> => {
  const id = route.params.id;
  if (id) {
    return inject(ExamService)
      .find(id)
      .pipe(
        mergeMap((exam: HttpResponse<IExam>) => {
          if (exam.body) {
            return of(exam.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default examResolve;
