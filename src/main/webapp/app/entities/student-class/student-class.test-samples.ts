import { IStudentClass, NewStudentClass } from './student-class.model';

export const sampleWithRequiredData: IStudentClass = {
  id: 31525,
  name: 'prestataire de services',
};

export const sampleWithPartialData: IStudentClass = {
  id: 13035,
  name: 'auprès de magenta',
};

export const sampleWithFullData: IStudentClass = {
  id: 26386,
  name: 'direction conseil d’administration',
  studentCount: 21610,
};

export const sampleWithNewData: NewStudentClass = {
  name: 'ça derechef',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
