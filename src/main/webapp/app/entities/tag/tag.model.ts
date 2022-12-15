import { ICliente } from 'app/entities/cliente/cliente.model';

export interface ITag {
  id: number;
  nome?: string | null;
  clientes?: Pick<ICliente, 'id'>[] | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
