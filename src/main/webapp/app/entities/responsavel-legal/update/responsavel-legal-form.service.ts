import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResponsavelLegal, NewResponsavelLegal } from '../responsavel-legal.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResponsavelLegal for edit and NewResponsavelLegalFormGroupInput for create.
 */
type ResponsavelLegalFormGroupInput = IResponsavelLegal | PartialWithRequiredKeyOf<NewResponsavelLegal>;

type ResponsavelLegalFormDefaults = Pick<NewResponsavelLegal, 'id'>;

type ResponsavelLegalFormGroupContent = {
  id: FormControl<IResponsavelLegal['id'] | NewResponsavelLegal['id']>;
  nome: FormControl<IResponsavelLegal['nome']>;
  cpf: FormControl<IResponsavelLegal['cpf']>;
};

export type ResponsavelLegalFormGroup = FormGroup<ResponsavelLegalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResponsavelLegalFormService {
  createResponsavelLegalFormGroup(responsavelLegal: ResponsavelLegalFormGroupInput = { id: null }): ResponsavelLegalFormGroup {
    const responsavelLegalRawValue = {
      ...this.getFormDefaults(),
      ...responsavelLegal,
    };
    return new FormGroup<ResponsavelLegalFormGroupContent>({
      id: new FormControl(
        { value: responsavelLegalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(responsavelLegalRawValue.nome, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(responsavelLegalRawValue.cpf, {
        validators: [Validators.required],
      }),
    });
  }

  getResponsavelLegal(form: ResponsavelLegalFormGroup): IResponsavelLegal | NewResponsavelLegal {
    return form.getRawValue() as IResponsavelLegal | NewResponsavelLegal;
  }

  resetForm(form: ResponsavelLegalFormGroup, responsavelLegal: ResponsavelLegalFormGroupInput): void {
    const responsavelLegalRawValue = { ...this.getFormDefaults(), ...responsavelLegal };
    form.reset(
      {
        ...responsavelLegalRawValue,
        id: { value: responsavelLegalRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResponsavelLegalFormDefaults {
    return {
      id: null,
    };
  }
}
