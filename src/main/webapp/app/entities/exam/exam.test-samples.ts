import { IExam, NewExam } from './exam.model';

export const sampleWithRequiredData: IExam = {
  id: 27404,
  name: 'candide que',
};

export const sampleWithPartialData: IExam = {
  id: 27803,
  name: 'tant que',
};

export const sampleWithFullData: IExam = {
  id: 4634,
  name: 'vouh',
};

export const sampleWithNewData: NewExam = {
  name: 'avant que collègue géométrique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
