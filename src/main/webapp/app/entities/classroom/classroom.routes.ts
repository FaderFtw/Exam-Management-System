import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ClassroomResolve from './route/classroom-routing-resolve.service';

const classroomRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/classroom.component').then(m => m.ClassroomComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/classroom-detail.component').then(m => m.ClassroomDetailComponent),
    resolve: {
      classroom: ClassroomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/classroom-update.component').then(m => m.ClassroomUpdateComponent),
    resolve: {
      classroom: ClassroomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/classroom-update.component').then(m => m.ClassroomUpdateComponent),
    resolve: {
      classroom: ClassroomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default classroomRoute;
