import { IInstitute, NewInstitute } from './institute.model';

export const sampleWithRequiredData: IInstitute = {
  id: 27959,
  name: 'simple adversaire',
  location: 'ah sincère ferme',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithPartialData: IInstitute = {
  id: 20331,
  name: "alentour à l'exception de",
  location: 'conseil d’administration approximativement',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  phone: '+33 475445957',
};

export const sampleWithFullData: IInstitute = {
  id: 23846,
  name: 'à défaut de  au défaut de croâ',
  location: 'en decà de',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  phone: '0315709418',
  email: 'Dieudonnee.Masson74@hotmail.fr',
  website: 'impromptu',
};

export const sampleWithNewData: NewInstitute = {
  name: 'au-dehors spécialiste baisser',
  location: 'alors que de façon à ce que',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
