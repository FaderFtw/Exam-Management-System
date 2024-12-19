import { IExam, NewExam } from './exam.model';

export const sampleWithRequiredData: IExam = {
  id: 16271,
  name: 'tendre police tic-tac',
};

export const sampleWithPartialData: IExam = {
  id: 6970,
  name: 'arrière',
};

export const sampleWithFullData: IExam = {
  id: 11008,
  name: 'au prix de',
};

export const sampleWithNewData: NewExam = {
  name: 'affronter',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
