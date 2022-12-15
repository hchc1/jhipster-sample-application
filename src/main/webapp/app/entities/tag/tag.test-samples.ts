import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 42372,
  nome: 'human-resource',
};

export const sampleWithPartialData: ITag = {
  id: 72152,
  nome: 'Officer Prático Extended',
};

export const sampleWithFullData: ITag = {
  id: 82371,
  nome: 'Micronésia',
};

export const sampleWithNewData: NewTag = {
  nome: 'heuristic next-generation',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
