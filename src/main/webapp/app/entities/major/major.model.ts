import { IStudentClass } from 'app/entities/student-class/student-class.model';

export interface IMajor {
  id: number;
  name?: string | null;
  studentClass?: Pick<IStudentClass, 'id'> | null;
}

export type NewMajor = Omit<IMajor, 'id'> & { id: null };
