import { IInstitute } from 'app/entities/institute/institute.model';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';

export interface IDepartment {
  id: number;
  name?: string | null;
  email?: string | null;
  institute?: Pick<IInstitute, 'id'> | null;
  examSessions?: Pick<IExamSession, 'id'>[] | null;
}

export type NewDepartment = Omit<IDepartment, 'id'> & { id: null };
