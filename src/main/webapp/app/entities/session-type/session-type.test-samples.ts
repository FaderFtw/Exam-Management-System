import { ISessionType, NewSessionType } from './session-type.model';

export const sampleWithRequiredData: ISessionType = {
  id: 32636,
  name: 'trop',
};

export const sampleWithPartialData: ISessionType = {
  id: 26484,
  name: 'près guide',
};

export const sampleWithFullData: ISessionType = {
  id: 29615,
  name: 'population du Québec incognito porte-parole',
};

export const sampleWithNewData: NewSessionType = {
  name: 'en dehors de adorable fonctionnaire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
