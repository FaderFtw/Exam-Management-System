import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAcademicInfo } from '../user-academic-info.model';
import { UserAcademicInfoService } from '../service/user-academic-info.service';

const userAcademicInfoResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserAcademicInfo> => {
  const id = route.params.id;
  if (id) {
    return inject(UserAcademicInfoService)
      .find(id)
      .pipe(
        mergeMap((userAcademicInfo: HttpResponse<IUserAcademicInfo>) => {
          if (userAcademicInfo.body) {
            return of(userAcademicInfo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userAcademicInfoResolve;
