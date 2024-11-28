import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TimetableResolve from './route/timetable-routing-resolve.service';

const timetableRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/timetable.component').then(m => m.TimetableComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/timetable-detail.component').then(m => m.TimetableDetailComponent),
    resolve: {
      timetable: TimetableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/timetable-update.component').then(m => m.TimetableUpdateComponent),
    resolve: {
      timetable: TimetableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/timetable-update.component').then(m => m.TimetableUpdateComponent),
    resolve: {
      timetable: TimetableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default timetableRoute;
