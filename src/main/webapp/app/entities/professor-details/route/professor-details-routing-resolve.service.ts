import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfessorDetails } from '../professor-details.model';
import { ProfessorDetailsService } from '../service/professor-details.service';

const professorDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | IProfessorDetails> => {
  const id = route.params.id;
  if (id) {
    return inject(ProfessorDetailsService)
      .find(id)
      .pipe(
        mergeMap((professorDetails: HttpResponse<IProfessorDetails>) => {
          if (professorDetails.body) {
            return of(professorDetails.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default professorDetailsResolve;
