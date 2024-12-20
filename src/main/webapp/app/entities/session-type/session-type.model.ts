export interface ISessionType {
  id: number;
  name?: string | null;
}

export type NewSessionType = Omit<ISessionType, 'id'> & { id: null };
