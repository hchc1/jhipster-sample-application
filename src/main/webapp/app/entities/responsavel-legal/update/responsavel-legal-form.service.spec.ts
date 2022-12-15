import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../responsavel-legal.test-samples';

import { ResponsavelLegalFormService } from './responsavel-legal-form.service';

describe('ResponsavelLegal Form Service', () => {
  let service: ResponsavelLegalFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResponsavelLegalFormService);
  });

  describe('Service methods', () => {
    describe('createResponsavelLegalFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResponsavelLegalFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cpf: expect.any(Object),
          })
        );
      });

      it('passing IResponsavelLegal should create a new form with FormGroup', () => {
        const formGroup = service.createResponsavelLegalFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cpf: expect.any(Object),
          })
        );
      });
    });

    describe('getResponsavelLegal', () => {
      it('should return NewResponsavelLegal for default ResponsavelLegal initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createResponsavelLegalFormGroup(sampleWithNewData);

        const responsavelLegal = service.getResponsavelLegal(formGroup) as any;

        expect(responsavelLegal).toMatchObject(sampleWithNewData);
      });

      it('should return NewResponsavelLegal for empty ResponsavelLegal initial value', () => {
        const formGroup = service.createResponsavelLegalFormGroup();

        const responsavelLegal = service.getResponsavelLegal(formGroup) as any;

        expect(responsavelLegal).toMatchObject({});
      });

      it('should return IResponsavelLegal', () => {
        const formGroup = service.createResponsavelLegalFormGroup(sampleWithRequiredData);

        const responsavelLegal = service.getResponsavelLegal(formGroup) as any;

        expect(responsavelLegal).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResponsavelLegal should not enable id FormControl', () => {
        const formGroup = service.createResponsavelLegalFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResponsavelLegal should disable id FormControl', () => {
        const formGroup = service.createResponsavelLegalFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
