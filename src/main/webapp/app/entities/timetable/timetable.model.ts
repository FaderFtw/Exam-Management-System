import dayjs from 'dayjs/esm';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';

export interface ITimetable {
  id: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  professor?: Pick<IProfessorDetails, 'id'> | null;
}

export type NewTimetable = Omit<ITimetable, 'id'> & { id: null };
