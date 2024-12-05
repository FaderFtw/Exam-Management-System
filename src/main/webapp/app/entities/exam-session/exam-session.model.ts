import dayjs from 'dayjs/esm';
import { ISessionType } from 'app/entities/session-type/session-type.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IExam } from 'app/entities/exam/exam.model';
import { IReport } from 'app/entities/report/report.model';

export interface IExamSession {
  id: number;
  name?: string | null;
  sessionCode?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  allowParallelStudies?: boolean | null;
  allowOwnClassSupervision?: boolean | null;
  allowCombineClasses?: boolean | null;
  sessionType?: Pick<ISessionType, 'id'> | null;
  departments?: Pick<IDepartment, 'id'>[] | null;
  exam?: Pick<IExam, 'id'> | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewExamSession = Omit<IExamSession, 'id'> & { id: null };