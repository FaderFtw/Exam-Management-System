import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SessionTypeResolve from './route/session-type-routing-resolve.service';

const sessionTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/session-type.component').then(m => m.SessionTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/session-type-detail.component').then(m => m.SessionTypeDetailComponent),
    resolve: {
      sessionType: SessionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/session-type-update.component').then(m => m.SessionTypeUpdateComponent),
    resolve: {
      sessionType: SessionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/session-type-update.component').then(m => m.SessionTypeUpdateComponent),
    resolve: {
      sessionType: SessionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sessionTypeRoute;
