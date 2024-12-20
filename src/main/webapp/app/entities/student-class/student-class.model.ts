import { IMajor } from 'app/entities/major/major.model';

export interface IStudentClass {
  id: number;
  name?: string | null;
  studentCount?: number | null;
  major?: Pick<IMajor, 'id'> | null;
}

export type NewStudentClass = Omit<IStudentClass, 'id'> & { id: null };
