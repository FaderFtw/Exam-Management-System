import { IUser } from 'app/entities/user/user.model';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { IMajor } from 'app/entities/major/major.model';
import { IReport } from 'app/entities/report/report.model';

export interface IDepartment {
  id: number;
  name?: string | null;
  email?: string | null;
  users?: Pick<IUser, 'id'> | null;
  examSessions?: Pick<IExamSession, 'id'>[] | null;
  classroom?: Pick<IClassroom, 'id'> | null;
  major?: Pick<IMajor, 'id'> | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
