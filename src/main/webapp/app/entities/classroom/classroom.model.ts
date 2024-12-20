import { IDepartment } from 'app/entities/department/department.model';

export interface IClassroom {
  id: number;
  name?: string | null;
  capacity?: number | null;
  department?: Pick<IDepartment, 'id'> | null;
}

export type NewClassroom = Omit<IClassroom, 'id'> & { id: null };
