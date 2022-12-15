import dayjs from 'dayjs/esm';
import { IResponsavelLegal } from 'app/entities/responsavel-legal/responsavel-legal.model';
import { ITag } from 'app/entities/tag/tag.model';

export interface ICliente {
  id: number;
  nome?: string | null;
  cpf?: string | null;
  rg?: string | null;
  foto?: string | null;
  fotoContentType?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  beneficioAtivo?: boolean | null;
  responsavelLegal?: Pick<IResponsavelLegal, 'id' | 'nome'> | null;
  tags?: Pick<ITag, 'id' | 'nome'>[] | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
