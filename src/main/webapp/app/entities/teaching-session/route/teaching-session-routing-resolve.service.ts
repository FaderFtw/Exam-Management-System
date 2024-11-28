import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeachingSession } from '../teaching-session.model';
import { TeachingSessionService } from '../service/teaching-session.service';

const teachingSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITeachingSession> => {
  const id = route.params.id;
  if (id) {
    return inject(TeachingSessionService)
      .find(id)
      .pipe(
        mergeMap((teachingSession: HttpResponse<ITeachingSession>) => {
          if (teachingSession.body) {
            return of(teachingSession.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default teachingSessionResolve;
