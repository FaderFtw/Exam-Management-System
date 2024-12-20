import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMajor } from '../major.model';
import { MajorService } from '../service/major.service';

const majorResolve = (route: ActivatedRouteSnapshot): Observable<null | IMajor> => {
  const id = route.params.id;
  if (id) {
    return inject(MajorService)
      .find(id)
      .pipe(
        mergeMap((major: HttpResponse<IMajor>) => {
          if (major.body) {
            return of(major.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default majorResolve;
