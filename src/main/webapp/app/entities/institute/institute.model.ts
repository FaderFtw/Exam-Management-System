import { IDepartment } from 'app/entities/department/department.model';
import { IReport } from 'app/entities/report/report.model';

export interface IInstitute {
  id: number;
  name?: string | null;
  location?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  phone?: string | null;
  email?: string | null;
  website?: string | null;
  department?: Pick<IDepartment, 'id'> | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewInstitute = Omit<IInstitute, 'id'> & { id: null };
