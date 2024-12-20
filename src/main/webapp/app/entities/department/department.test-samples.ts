import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 19139,
  name: 'prout lâche',
};

export const sampleWithPartialData: IDepartment = {
  id: 15112,
  name: 'sage à cause de hi',
  email: 'Absalon_Robin7@hotmail.fr',
};

export const sampleWithFullData: IDepartment = {
  id: 8143,
  name: 'pendant magnifique',
  email: 'Anceline.Leclercq60@gmail.com',
};

export const sampleWithNewData: NewDepartment = {
  name: 'lorsque',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
