import dayjs from 'dayjs/esm';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { IStudentClass } from 'app/entities/student-class/student-class.model';
import { IExamSession } from 'app/entities/exam-session/exam-session.model';
import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';

export interface IExam {
  id: number;
  name?: string | null;
  numberOfStudents?: number | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  classroom?: Pick<IClassroom, 'id'> | null;
  studentClass?: Pick<IStudentClass, 'id'> | null;
  session?: Pick<IExamSession, 'id'> | null;
  supervisors?: Pick<IProfessorDetails, 'id'>[] | null;
}

export type NewExam = Omit<IExam, 'id'> & { id: null };
