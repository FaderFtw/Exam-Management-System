import dayjs from 'dayjs/esm';

import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: 19198,
  name: 'au-dessous désagréable paf',
  createdDate: dayjs('2024-11-27T20:45'),
};

export const sampleWithPartialData: IReport = {
  id: 32347,
  name: 'impromptu',
  createdDate: dayjs('2024-11-28T11:22'),
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReport = {
  id: 14100,
  name: 'toc-toc calmer écouter',
  createdDate: dayjs('2024-11-28T07:42'),
  content: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReport = {
  name: 'auparavant',
  createdDate: dayjs('2024-11-28T11:42'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
