import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '446d44e4-514e-415b-81c8-b369fa366854',
};

export const sampleWithPartialData: IAuthority = {
  name: '754143a9-22ca-4245-a423-c7b38d72cb1f',
};

export const sampleWithFullData: IAuthority = {
  name: '0c714ad0-e4e9-433a-8d32-68a94821c2d0',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
