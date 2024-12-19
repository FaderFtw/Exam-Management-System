import { IClassroom, NewClassroom } from './classroom.model';

export const sampleWithRequiredData: IClassroom = {
  id: 20296,
  name: 'de façon à ce que',
  capacity: 29062,
};

export const sampleWithPartialData: IClassroom = {
  id: 6730,
  name: "à l'instar de",
  capacity: 26636,
};

export const sampleWithFullData: IClassroom = {
  id: 29970,
  name: 'simple mature',
  capacity: 27147,
};

export const sampleWithNewData: NewClassroom = {
  name: 'préparer après que chef de cuisine',
  capacity: 14331,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
