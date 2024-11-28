import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InstituteResolve from './route/institute-routing-resolve.service';

const instituteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/institute.component').then(m => m.InstituteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/institute-detail.component').then(m => m.InstituteDetailComponent),
    resolve: {
      institute: InstituteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/institute-update.component').then(m => m.InstituteUpdateComponent),
    resolve: {
      institute: InstituteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/institute-update.component').then(m => m.InstituteUpdateComponent),
    resolve: {
      institute: InstituteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default instituteRoute;
