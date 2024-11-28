import { IInstitute, NewInstitute } from './institute.model';

export const sampleWithRequiredData: IInstitute = {
  id: 7980,
  name: 'clientèle de crainte que',
  location: 'du moment que affecter personnel',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithPartialData: IInstitute = {
  id: 24685,
  name: 'déjà',
  location: 'ha',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithFullData: IInstitute = {
  id: 15348,
  name: 'ouf toc-toc',
  location: 'gens concurrence',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  phone: '+33 192115925',
  email: 'Maureen.Gerard32@gmail.com',
  website: 'dès supposer',
};

export const sampleWithNewData: NewInstitute = {
  name: 'groin groin',
  location: 'administrer sous puisque',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
