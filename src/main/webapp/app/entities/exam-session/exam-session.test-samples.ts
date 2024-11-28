import dayjs from 'dayjs/esm';

import { IExamSession, NewExamSession } from './exam-session.model';

export const sampleWithRequiredData: IExamSession = {
  id: 20414,
  name: 'venir complètement',
  sessionCode: 'enfin secouriste dépendre',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: false,
  allowOwnClassSupervision: false,
  allowCombineClasses: false,
};

export const sampleWithPartialData: IExamSession = {
  id: 19088,
  name: 'détendre',
  sessionCode: 'commis pressentir',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: true,
  allowOwnClassSupervision: true,
  allowCombineClasses: false,
};

export const sampleWithFullData: IExamSession = {
  id: 6346,
  name: "d'après",
  sessionCode: 'surveiller collègue hors',
  startDate: dayjs('2024-11-28'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: true,
  allowOwnClassSupervision: true,
  allowCombineClasses: true,
};

export const sampleWithNewData: NewExamSession = {
  name: 'membre à vie à travers',
  sessionCode: 'cot cot',
  startDate: dayjs('2024-11-27'),
  endDate: dayjs('2024-11-28'),
  allowParallelStudies: false,
  allowOwnClassSupervision: true,
  allowCombineClasses: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
