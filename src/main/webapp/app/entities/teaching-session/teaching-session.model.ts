import dayjs from 'dayjs/esm';
import { ITimetable } from 'app/entities/timetable/timetable.model';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { IClassroom } from 'app/entities/classroom/classroom.model';

export interface ITeachingSession {
  id: number;
  name?: string | null;
  startHour?: dayjs.Dayjs | null;
  endHour?: dayjs.Dayjs | null;
  type?: string | null;
  timetable?: Pick<ITimetable, 'id'> | null;
  studentClass?: Pick<IStudentClass, 'id'> | null;
  classroom?: Pick<IClassroom, 'id'> | null;
}

export type NewTeachingSession = Omit<ITeachingSession, 'id'> & { id: null };
