import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id' | 'beneficioAtivo' | 'tags'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  nome: FormControl<ICliente['nome']>;
  cpf: FormControl<ICliente['cpf']>;
  rg: FormControl<ICliente['rg']>;
  foto: FormControl<ICliente['foto']>;
  fotoContentType: FormControl<ICliente['fotoContentType']>;
  dataNascimento: FormControl<ICliente['dataNascimento']>;
  beneficioAtivo: FormControl<ICliente['beneficioAtivo']>;
  responsavelLegal: FormControl<ICliente['responsavelLegal']>;
  tags: FormControl<ICliente['tags']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente: ClienteFormGroupInput = { id: null }): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...cliente,
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(clienteRawValue.nome, {
        validators: [Validators.required],
      }),
      cpf: new FormControl(clienteRawValue.cpf, {
        validators: [Validators.required],
      }),
      rg: new FormControl(clienteRawValue.rg, {
        validators: [Validators.required],
      }),
      foto: new FormControl(clienteRawValue.foto),
      fotoContentType: new FormControl(clienteRawValue.fotoContentType),
      dataNascimento: new FormControl(clienteRawValue.dataNascimento, {
        validators: [Validators.required],
      }),
      beneficioAtivo: new FormControl(clienteRawValue.beneficioAtivo),
      responsavelLegal: new FormControl(clienteRawValue.responsavelLegal),
      tags: new FormControl(clienteRawValue.tags ?? []),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset(
      {
        ...clienteRawValue,
        id: { value: clienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
      beneficioAtivo: false,
      tags: [],
    };
  }
}
