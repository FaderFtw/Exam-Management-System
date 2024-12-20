import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstitute } from '../institute.model';
import { InstituteService } from '../service/institute.service';

const instituteResolve = (route: ActivatedRouteSnapshot): Observable<null | IInstitute> => {
  const id = route.params.id;
  if (id) {
    return inject(InstituteService)
      .find(id)
      .pipe(
        mergeMap((institute: HttpResponse<IInstitute>) => {
          if (institute.body) {
            return of(institute.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default instituteResolve;
