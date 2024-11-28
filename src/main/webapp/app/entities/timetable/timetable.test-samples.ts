import dayjs from 'dayjs/esm';

import { ITimetable, NewTimetable } from './timetable.model';

export const sampleWithRequiredData: ITimetable = {
  id: 999,
  name: 'fort soutenir en bas de',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-27'),
};

export const sampleWithPartialData: ITimetable = {
  id: 10431,
  name: 'choisir claquer psitt',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-27'),
};

export const sampleWithFullData: ITimetable = {
  id: 23741,
  name: 'ouah touriste',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
};

export const sampleWithNewData: NewTimetable = {
  name: 'ouah sauvegarder',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
