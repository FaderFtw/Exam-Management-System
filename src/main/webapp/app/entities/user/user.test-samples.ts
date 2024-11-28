import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 20140,
  login: 'J95X',
};

export const sampleWithPartialData: IUser = {
  id: 9313,
  login: 'a@m',
};

export const sampleWithFullData: IUser = {
  id: 6949,
  login: 'fbJ_@UOOAbj\\Ik\\3KwBw1\\7Uis8G',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
