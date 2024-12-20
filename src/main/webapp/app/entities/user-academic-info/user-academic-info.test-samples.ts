import { IUserAcademicInfo, NewUserAcademicInfo } from './user-academic-info.model';

export const sampleWithRequiredData: IUserAcademicInfo = {
  id: 13578,
};

export const sampleWithPartialData: IUserAcademicInfo = {
  id: 6832,
};

export const sampleWithFullData: IUserAcademicInfo = {
  id: 6615,
};

export const sampleWithNewData: NewUserAcademicInfo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
