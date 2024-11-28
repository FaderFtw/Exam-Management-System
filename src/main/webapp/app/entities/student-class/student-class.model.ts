import { IExam } from 'app/entities/exam/exam.model';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';

export interface IStudentClass {
  id: number;
  name?: string | null;
  studentCount?: number | null;
  exam?: Pick<IExam, 'id'> | null;
  teachingSession?: Pick<ITeachingSession, 'id'> | null;
}

export type NewStudentClass = Omit<IStudentClass, 'id'> & { id: null };
