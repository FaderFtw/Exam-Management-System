import { IStudentClass, NewStudentClass } from './student-class.model';

export const sampleWithRequiredData: IStudentClass = {
  id: 9212,
  name: 'déjà avant que',
};

export const sampleWithPartialData: IStudentClass = {
  id: 8287,
  name: 'poursuivre électorat pschitt',
  studentCount: 6433,
};

export const sampleWithFullData: IStudentClass = {
  id: 25229,
  name: 'drôlement miam ouch',
  studentCount: 11614,
};

export const sampleWithNewData: NewStudentClass = {
  name: 'hors immense jeune enfant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
