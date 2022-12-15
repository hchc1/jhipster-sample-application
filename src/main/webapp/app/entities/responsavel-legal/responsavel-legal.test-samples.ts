import { IResponsavelLegal, NewResponsavelLegal } from './responsavel-legal.model';

export const sampleWithRequiredData: IResponsavelLegal = {
  id: 29803,
  nome: 'Pizza Architect',
  cpf: 'Feito withdrawal',
};

export const sampleWithPartialData: IResponsavelLegal = {
  id: 41573,
  nome: 'Supervisor',
  cpf: 'Cambridgeshire',
};

export const sampleWithFullData: IResponsavelLegal = {
  id: 40524,
  nome: 'Nova Marginal',
  cpf: 'deliver didactic',
};

export const sampleWithNewData: NewResponsavelLegal = {
  nome: 'mobile Eletrônicos non-volatile',
  cpf: 'escarlate Música',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
