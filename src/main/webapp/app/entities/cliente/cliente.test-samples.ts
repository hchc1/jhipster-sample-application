import dayjs from 'dayjs/esm';

import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 45820,
  nome: 'Pará',
  cpf: 'Saúde',
  rg: 'grow Grosso',
  dataNascimento: dayjs('2022-12-14'),
};

export const sampleWithPartialData: ICliente = {
  id: 87424,
  nome: 'Automotivo Platinum',
  cpf: 'Bicicleta',
  rg: 'navigate streamline',
  dataNascimento: dayjs('2022-12-15'),
};

export const sampleWithFullData: ICliente = {
  id: 99916,
  nome: 'JBOD invoice',
  cpf: 'Qatari framework',
  rg: 'Nauru Algodão granular',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
  dataNascimento: dayjs('2022-12-15'),
  beneficioAtivo: false,
};

export const sampleWithNewData: NewCliente = {
  nome: 'haptic',
  cpf: 'niches',
  rg: 'Central',
  dataNascimento: dayjs('2022-12-14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
