import { IProfessorDetails, NewProfessorDetails } from './professor-details.model';

export const sampleWithRequiredData: IProfessorDetails = {
  id: 20193,
  rank: 'CONTRACTUEL',
};

export const sampleWithPartialData: IProfessorDetails = {
  id: 18392,
  rank: 'EXPERT',
};

export const sampleWithFullData: IProfessorDetails = {
  id: 32043,
  rank: 'MAITRE_ASSISTANT',
};

export const sampleWithNewData: NewProfessorDetails = {
  rank: 'PES',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
