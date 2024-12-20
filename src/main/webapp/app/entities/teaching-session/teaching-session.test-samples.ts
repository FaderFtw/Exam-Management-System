import dayjs from 'dayjs/esm';

import { ITeachingSession, NewTeachingSession } from './teaching-session.model';

export const sampleWithRequiredData: ITeachingSession = {
  id: 21246,
  name: 'lâcher gêner',
  startHour: dayjs('2024-11-28T11:32'),
  endHour: dayjs('2024-11-28T09:08'),
  type: 'dynamique tandis que',
};

export const sampleWithPartialData: ITeachingSession = {
  id: 1096,
  name: 'pendant que environ',
  startHour: dayjs('2024-11-27T21:52'),
  endHour: dayjs('2024-11-27T23:28'),
  type: 'conseil d’administration',
};

export const sampleWithFullData: ITeachingSession = {
  id: 24041,
  name: 'rédaction rectorat attaquer',
  startHour: dayjs('2024-11-28T07:43'),
  endHour: dayjs('2024-11-27T21:59'),
  type: 'désormais énorme longtemps',
};

export const sampleWithNewData: NewTeachingSession = {
  name: 'tsoin-tsoin pourpre dense',
  startHour: dayjs('2024-11-28T16:55'),
  endHour: dayjs('2024-11-28T07:22'),
  type: 'de crainte que malade',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
