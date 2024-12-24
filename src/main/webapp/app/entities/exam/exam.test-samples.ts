import dayjs from 'dayjs/esm';

import { IExam, NewExam } from './exam.model';

export const sampleWithRequiredData: IExam = {
  id: 15459,
  name: 'environ violer contre',
  numberOfStudents: 4666,
  startTime: dayjs('2024-11-28T14:14'),
  endTime: dayjs('2024-11-28T16:55'),
};

export const sampleWithPartialData: IExam = {
  id: 30473,
  name: 'peut-être à condition que',
  numberOfStudents: 5129,
  startTime: dayjs('2024-11-28T15:38'),
  endTime: dayjs('2024-11-28T12:03'),
};

export const sampleWithFullData: IExam = {
  id: 1212,
  name: 'de façon que résigner tant',
  numberOfStudents: 2704,
  startTime: dayjs('2024-11-28T16:23'),
  endTime: dayjs('2024-11-28T17:26'),
};

export const sampleWithNewData: NewExam = {
  name: 'prononcer au moyen de',
  numberOfStudents: 8280,
  startTime: dayjs('2024-11-28T11:10'),
  endTime: dayjs('2024-11-28T01:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
