import dayjs from 'dayjs/esm';

export interface ITeachingSession {
  id: number;
  name?: string | null;
  startHour?: dayjs.Dayjs | null;
  endHour?: dayjs.Dayjs | null;
  type?: string | null;
}

export type NewTeachingSession = Omit<ITeachingSession, 'id'> & { id: null };
