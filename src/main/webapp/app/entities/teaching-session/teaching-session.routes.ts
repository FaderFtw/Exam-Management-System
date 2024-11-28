import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TeachingSessionResolve from './route/teaching-session-routing-resolve.service';

const teachingSessionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/teaching-session.component').then(m => m.TeachingSessionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/teaching-session-detail.component').then(m => m.TeachingSessionDetailComponent),
    resolve: {
      teachingSession: TeachingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/teaching-session-update.component').then(m => m.TeachingSessionUpdateComponent),
    resolve: {
      teachingSession: TeachingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/teaching-session-update.component').then(m => m.TeachingSessionUpdateComponent),
    resolve: {
      teachingSession: TeachingSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default teachingSessionRoute;
