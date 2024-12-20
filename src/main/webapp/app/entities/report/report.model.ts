import dayjs from 'dayjs/esm';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IInstitute } from 'app/entities/institute/institute.model';

export interface IReport {
  id: number;
  name?: string | null;
  createdDate?: dayjs.Dayjs | null;
  content?: string | null;
  professor?: Pick<IProfessorDetails, 'id'> | null;
  examSession?: Pick<IExamSession, 'id'> | null;
  department?: Pick<IDepartment, 'id'> | null;
  institute?: Pick<IInstitute, 'id'> | null;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
