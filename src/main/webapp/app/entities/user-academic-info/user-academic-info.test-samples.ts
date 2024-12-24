import { IUserAcademicInfo, NewUserAcademicInfo } from './user-academic-info.model';

export const sampleWithRequiredData: IUserAcademicInfo = {
  id: 15487,
};

export const sampleWithPartialData: IUserAcademicInfo = {
  id: 4799,
};

export const sampleWithFullData: IUserAcademicInfo = {
  id: 16833,
  phone: '83710045',
};

export const sampleWithNewData: NewUserAcademicInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
