import { IMajor, NewMajor } from './major.model';

export const sampleWithRequiredData: IMajor = {
  id: 27837,
  name: 'psitt',
};

export const sampleWithPartialData: IMajor = {
  id: 8168,
  name: 'moderne',
};

export const sampleWithFullData: IMajor = {
  id: 14010,
  name: 'de manière à ce que',
};

export const sampleWithNewData: NewMajor = {
  name: 'concurrence',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
