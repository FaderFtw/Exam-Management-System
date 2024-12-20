import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MajorResolve from './route/major-routing-resolve.service';

const majorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/major.component').then(m => m.MajorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/major-detail.component').then(m => m.MajorDetailComponent),
    resolve: {
      major: MajorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/major-update.component').then(m => m.MajorUpdateComponent),
    resolve: {
      major: MajorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/major-update.component').then(m => m.MajorUpdateComponent),
    resolve: {
      major: MajorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default majorRoute;
