import dayjs from 'dayjs/esm';

import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: 21519,
  name: "bè quoique à l'encontre de",
  createdDate: dayjs('2024-11-28T08:50'),
};

export const sampleWithPartialData: IReport = {
  id: 8393,
  name: 'foncer bzzz auparavant',
  createdDate: dayjs('2024-11-27T20:56'),
};

export const sampleWithFullData: IReport = {
  id: 5101,
  name: 'moins servir',
  createdDate: dayjs('2024-11-27T21:29'),
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReport = {
  name: 'assez timide égoïste',
  createdDate: dayjs('2024-11-28T16:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
