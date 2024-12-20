import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReport } from '../report.model';
import { ReportService } from '../service/report.service';

const reportResolve = (route: ActivatedRouteSnapshot): Observable<null | IReport> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportService)
      .find(id)
      .pipe(
        mergeMap((report: HttpResponse<IReport>) => {
          if (report.body) {
            return of(report.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportResolve;
