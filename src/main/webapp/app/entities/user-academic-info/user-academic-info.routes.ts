import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserAcademicInfoResolve from './route/user-academic-info-routing-resolve.service';

const userAcademicInfoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-academic-info.component').then(m => m.UserAcademicInfoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-academic-info-detail.component').then(m => m.UserAcademicInfoDetailComponent),
    resolve: {
      userAcademicInfo: UserAcademicInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-academic-info-update.component').then(m => m.UserAcademicInfoUpdateComponent),
    resolve: {
      userAcademicInfo: UserAcademicInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-academic-info-update.component').then(m => m.UserAcademicInfoUpdateComponent),
    resolve: {
      userAcademicInfo: UserAcademicInfoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userAcademicInfoRoute;
