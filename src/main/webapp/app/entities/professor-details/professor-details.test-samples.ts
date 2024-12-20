import { IProfessorDetails, NewProfessorDetails } from './professor-details.model';

export const sampleWithRequiredData: IProfessorDetails = {
  id: 18654,
  rank: 'MAITRE_ASSISTANT',
};

export const sampleWithPartialData: IProfessorDetails = {
  id: 18884,
  rank: 'MAITRE_CONF',
};

export const sampleWithFullData: IProfessorDetails = {
  id: 3149,
  rank: 'ASSISTANT',
};

export const sampleWithNewData: NewProfessorDetails = {
  rank: 'MAITRE_ASSISTANT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
