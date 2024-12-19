import { IDepartment } from 'app/entities/department/department.model';

export interface IMajor {
  id: number;
  name?: string | null;
  department?: Pick<IDepartment, 'id'> | null;
}

export type NewMajor = Omit<IMajor, 'id'> & { id: null };
