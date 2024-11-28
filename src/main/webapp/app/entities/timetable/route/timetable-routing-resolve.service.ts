import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITimetable } from '../timetable.model';
import { TimetableService } from '../service/timetable.service';

const timetableResolve = (route: ActivatedRouteSnapshot): Observable<null | ITimetable> => {
  const id = route.params.id;
  if (id) {
    return inject(TimetableService)
      .find(id)
      .pipe(
        mergeMap((timetable: HttpResponse<ITimetable>) => {
          if (timetable.body) {
            return of(timetable.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default timetableResolve;
