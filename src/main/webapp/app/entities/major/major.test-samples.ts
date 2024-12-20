import { IMajor, NewMajor } from './major.model';

export const sampleWithRequiredData: IMajor = {
  id: 17412,
  name: 'régner contre',
};

export const sampleWithPartialData: IMajor = {
  id: 23780,
  name: 'de crainte que extra',
};

export const sampleWithFullData: IMajor = {
  id: 11546,
  name: 'ouch au-dessous de',
};

export const sampleWithNewData: NewMajor = {
  name: 'au dépens de courir égoïste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
