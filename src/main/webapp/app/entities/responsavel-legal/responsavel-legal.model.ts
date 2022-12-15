export interface IResponsavelLegal {
  id: number;
  nome?: string | null;
  cpf?: string | null;
}

export type NewResponsavelLegal = Omit<IResponsavelLegal, 'id'> & { id: null };
