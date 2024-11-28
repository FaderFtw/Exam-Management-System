import { IProfessorDetails } from 'app/entities/professor-details/professor-details.model';

export interface IExam {
  id: number;
  name?: string | null;
  supervisors?: Pick<IProfessorDetails, 'id'>[] | null;
}

export type NewExam = Omit<IExam, 'id'> & { id: null };
