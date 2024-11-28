import dayjs from 'dayjs/esm';

import { ITeachingSession, NewTeachingSession } from './teaching-session.model';

export const sampleWithRequiredData: ITeachingSession = {
  id: 19962,
  name: 'fade vroum',
  startHour: dayjs('2024-11-28T03:54'),
  endHour: dayjs('2024-11-28T05:18'),
  type: 'résonner au lieu de moyennant',
};

export const sampleWithPartialData: ITeachingSession = {
  id: 18974,
  name: 'bang',
  startHour: dayjs('2024-11-28T04:33'),
  endHour: dayjs('2024-11-28T16:57'),
  type: 'de peur que patientèle',
};

export const sampleWithFullData: ITeachingSession = {
  id: 16857,
  name: 'd’autant que',
  startHour: dayjs('2024-11-28T04:20'),
  endHour: dayjs('2024-11-27T21:11'),
  type: 'en faveur de',
};

export const sampleWithNewData: NewTeachingSession = {
  name: 'bzzz jeune',
  startHour: dayjs('2024-11-27T22:24'),
  endHour: dayjs('2024-11-27T18:29'),
  type: 'assurément vers',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
