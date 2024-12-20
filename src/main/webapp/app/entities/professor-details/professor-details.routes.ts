import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProfessorDetailsResolve from './route/professor-details-routing-resolve.service';

const professorDetailsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/professor-details.component').then(m => m.ProfessorDetailsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/professor-details-detail.component').then(m => m.ProfessorDetailsDetailComponent),
    resolve: {
      professorDetails: ProfessorDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/professor-details-update.component').then(m => m.ProfessorDetailsUpdateComponent),
    resolve: {
      professorDetails: ProfessorDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/professor-details-update.component').then(m => m.ProfessorDetailsUpdateComponent),
    resolve: {
      professorDetails: ProfessorDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default professorDetailsRoute;
