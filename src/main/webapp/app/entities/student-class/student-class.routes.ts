import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import StudentClassResolve from './route/student-class-routing-resolve.service';

const studentClassRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/student-class.component').then(m => m.StudentClassComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/student-class-detail.component').then(m => m.StudentClassDetailComponent),
    resolve: {
      studentClass: StudentClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/student-class-update.component').then(m => m.StudentClassUpdateComponent),
    resolve: {
      studentClass: StudentClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/student-class-update.component').then(m => m.StudentClassUpdateComponent),
    resolve: {
      studentClass: StudentClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default studentClassRoute;
