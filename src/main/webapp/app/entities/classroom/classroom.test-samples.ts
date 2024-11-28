import { IClassroom, NewClassroom } from './classroom.model';

export const sampleWithRequiredData: IClassroom = {
  id: 27128,
  name: 'tendre peu',
  capacity: 7016,
};

export const sampleWithPartialData: IClassroom = {
  id: 2067,
  name: 'différencier assez',
  capacity: 3988,
};

export const sampleWithFullData: IClassroom = {
  id: 6495,
  name: 'ouin repasser',
  capacity: 11486,
};

export const sampleWithNewData: NewClassroom = {
  name: 'sitôt que au point que',
  capacity: 13666,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
