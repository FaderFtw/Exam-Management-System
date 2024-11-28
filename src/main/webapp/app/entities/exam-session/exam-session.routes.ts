import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ExamSessionResolve from './route/exam-session-routing-resolve.service';

const examSessionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/exam-session.component').then(m => m.ExamSessionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/exam-session-detail.component').then(m => m.ExamSessionDetailComponent),
    resolve: {
      examSession: ExamSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/exam-session-update.component').then(m => m.ExamSessionUpdateComponent),
    resolve: {
      examSession: ExamSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/exam-session-update.component').then(m => m.ExamSessionUpdateComponent),
    resolve: {
      examSession: ExamSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default examSessionRoute;
