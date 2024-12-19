export interface IInstitute {
  id: number;
  name?: string | null;
  location?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  phone?: string | null;
  email?: string | null;
  website?: string | null;
}

export type NewInstitute = Omit<IInstitute, 'id'> & { id: null };
