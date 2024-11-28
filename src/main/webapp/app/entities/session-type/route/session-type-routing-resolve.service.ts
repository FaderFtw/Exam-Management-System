import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionType } from '../session-type.model';
import { SessionTypeService } from '../service/session-type.service';

const sessionTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ISessionType> => {
  const id = route.params.id;
  if (id) {
    return inject(SessionTypeService)
      .find(id)
      .pipe(
        mergeMap((sessionType: HttpResponse<ISessionType>) => {
          if (sessionType.body) {
            return of(sessionType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default sessionTypeResolve;
