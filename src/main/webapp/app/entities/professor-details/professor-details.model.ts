import { IUser } from 'app/entities/user/user.model';
import { IExam } from 'app/entities/exam/exam.model';
import { IReport } from 'app/entities/report/report.model';
import { ITimetable } from 'app/entities/timetable/timetable.model';
import { Rank } from 'app/entities/enumerations/rank.model';

export interface IProfessorDetails {
  id: number;
  rank?: keyof typeof Rank | null;
  user?: Pick<IUser, 'id'> | null;
  supervisedExams?: Pick<IExam, 'id'>[] | null;
  report?: Pick<IReport, 'id'> | null;
  timetable?: Pick<ITimetable, 'id'> | null;
}

export type NewProfessorDetails = Omit<IProfessorDetails, 'id'> & { id: null };
