import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExamSession } from '../exam-session.model';
import { ExamSessionService } from '../service/exam-session.service';

const examSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | IExamSession> => {
  const id = route.params.id;
  if (id) {
    return inject(ExamSessionService)
      .find(id)
      .pipe(
        mergeMap((examSession: HttpResponse<IExamSession>) => {
          if (examSession.body) {
            return of(examSession.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default examSessionResolve;
