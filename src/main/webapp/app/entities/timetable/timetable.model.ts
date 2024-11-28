import dayjs from 'dayjs/esm';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';

export interface ITimetable {
  id: number;
  name?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  teachingSession?: Pick<ITeachingSession, 'id'> | null;
}

export type NewTimetable = Omit<ITimetable, 'id'> & { id: null };
