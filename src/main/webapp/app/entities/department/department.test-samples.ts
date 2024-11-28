import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 13750,
  name: 'intervenir',
};

export const sampleWithPartialData: IDepartment = {
  id: 15353,
  name: 'échanger bof',
};

export const sampleWithFullData: IDepartment = {
  id: 30958,
  name: 'assez envers contre',
  email: 'Armel.Berger62@hotmail.fr',
};

export const sampleWithNewData: NewDepartment = {
  name: 'énergique si clientèle',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
