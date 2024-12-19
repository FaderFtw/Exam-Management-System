import { IUser } from 'app/entities/user/user.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IInstitute } from 'app/entities/institute/institute.model';

export interface IUserAcademicInfo {
  id: number;
  user?: Pick<IUser, 'id' | 'login'> | null;
  department?: Pick<IDepartment, 'id' | 'name'> | null;
  institute?: Pick<IInstitute, 'id' | 'name'> | null;
}

export type NewUserAcademicInfo = Omit<IUserAcademicInfo, 'id'> & { id: null };
