import { ISessionType, NewSessionType } from './session-type.model';

export const sampleWithRequiredData: ISessionType = {
  id: 11431,
  name: 'entièrement confesser',
};

export const sampleWithPartialData: ISessionType = {
  id: 25756,
  name: "d'après",
};

export const sampleWithFullData: ISessionType = {
  id: 25831,
  name: 'briller ha ha plaider',
};

export const sampleWithNewData: NewSessionType = {
  name: 'saluer dorénavant à la faveur de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
