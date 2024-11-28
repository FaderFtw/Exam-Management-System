import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'examManagerApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'classroom',
    data: { pageTitle: 'examManagerApp.classroom.home.title' },
    loadChildren: () => import('./classroom/classroom.routes'),
  },
  {
    path: 'department',
    data: { pageTitle: 'examManagerApp.department.home.title' },
    loadChildren: () => import('./department/department.routes'),
  },
  {
    path: 'exam',
    data: { pageTitle: 'examManagerApp.exam.home.title' },
    loadChildren: () => import('./exam/exam.routes'),
  },
  {
    path: 'exam-session',
    data: { pageTitle: 'examManagerApp.examSession.home.title' },
    loadChildren: () => import('./exam-session/exam-session.routes'),
  },
  {
    path: 'institute',
    data: { pageTitle: 'examManagerApp.institute.home.title' },
    loadChildren: () => import('./institute/institute.routes'),
  },
  {
    path: 'major',
    data: { pageTitle: 'examManagerApp.major.home.title' },
    loadChildren: () => import('./major/major.routes'),
  },
  {
    path: 'professor-details',
    data: { pageTitle: 'examManagerApp.professorDetails.home.title' },
    loadChildren: () => import('./professor-details/professor-details.routes'),
  },
  {
    path: 'report',
    data: { pageTitle: 'examManagerApp.report.home.title' },
    loadChildren: () => import('./report/report.routes'),
  },
  {
    path: 'session-type',
    data: { pageTitle: 'examManagerApp.sessionType.home.title' },
    loadChildren: () => import('./session-type/session-type.routes'),
  },
  {
    path: 'student-class',
    data: { pageTitle: 'examManagerApp.studentClass.home.title' },
    loadChildren: () => import('./student-class/student-class.routes'),
  },
  {
    path: 'teaching-session',
    data: { pageTitle: 'examManagerApp.teachingSession.home.title' },
    loadChildren: () => import('./teaching-session/teaching-session.routes'),
  },
  {
    path: 'timetable',
    data: { pageTitle: 'examManagerApp.timetable.home.title' },
    loadChildren: () => import('./timetable/timetable.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
