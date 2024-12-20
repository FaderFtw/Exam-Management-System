import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ReportResolve from './route/report-routing-resolve.service';

const reportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/report.component').then(m => m.ReportComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/report-detail.component').then(m => m.ReportDetailComponent),
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/report-update.component').then(m => m.ReportUpdateComponent),
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/report-update.component').then(m => m.ReportUpdateComponent),
    resolve: {
      report: ReportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportRoute;
