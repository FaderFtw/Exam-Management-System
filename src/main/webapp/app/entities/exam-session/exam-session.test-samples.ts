import dayjs from 'dayjs/esm';

import { IExamSession, NewExamSession } from './exam-session.model';

export const sampleWithRequiredData: IExamSession = {
  id: 32391,
  name: 'que',
  sessionCode: 'à peine',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: true,
  allowOwnClassSupervision: true,
  allowCombineClasses: true,
};

export const sampleWithPartialData: IExamSession = {
  id: 7370,
  name: 'respirer habile au défaut de',
  sessionCode: 'bè initier',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: false,
  allowOwnClassSupervision: true,
  allowCombineClasses: false,
};

export const sampleWithFullData: IExamSession = {
  id: 2884,
  name: 'au prix de aussitôt que quelquefois',
  sessionCode: 'tchou tchouu',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: true,
  allowOwnClassSupervision: true,
  allowCombineClasses: true,
};

export const sampleWithNewData: NewExamSession = {
  name: 'par suite de membre de l’équipe',
  sessionCode: 'au point que',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-27'),
  allowParallelStudies: true,
  allowOwnClassSupervision: true,
  allowCombineClasses: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
