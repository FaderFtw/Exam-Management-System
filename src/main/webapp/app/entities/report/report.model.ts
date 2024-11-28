import dayjs from 'dayjs/esm';

export interface IReport {
  id: number;
  name?: string | null;
  createdDate?: dayjs.Dayjs | null;
  content?: string | null;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
