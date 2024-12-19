import dayjs from 'dayjs/esm';

import { ITimetable, NewTimetable } from './timetable.model';

export const sampleWithRequiredData: ITimetable = {
  id: 31176,
  name: 'snif à travers',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
};

export const sampleWithPartialData: ITimetable = {
  id: 9668,
  name: 'avant que',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
};

export const sampleWithFullData: ITimetable = {
  id: 31760,
  name: 'devant de peur que commissionnaire',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
};

export const sampleWithNewData: NewTimetable = {
  name: 'loin de de sorte que',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
