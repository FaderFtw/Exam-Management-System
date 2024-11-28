import { IExam } from 'app/entities/exam/exam.model';
import { ITeachingSession } from 'app/entities/teaching-session/teaching-session.model';

export interface IClassroom {
  id: number;
  name?: string | null;
  capacity?: number | null;
  exam?: Pick<IExam, 'id'> | null;
  teachingSession?: Pick<ITeachingSession, 'id'> | null;
}

export type NewClassroom = Omit<IClassroom, 'id'> & { id: null };
