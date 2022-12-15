import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ClienteFormService, ClienteFormGroup } from './cliente-form.service';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IResponsavelLegal } from 'app/entities/responsavel-legal/responsavel-legal.model';
import { ResponsavelLegalService } from 'app/entities/responsavel-legal/service/responsavel-legal.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  cliente: ICliente | null = null;

  responsavelLegalsSharedCollection: IResponsavelLegal[] = [];
  tagsSharedCollection: ITag[] = [];

  editForm: ClienteFormGroup = this.clienteFormService.createClienteFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected clienteService: ClienteService,
    protected clienteFormService: ClienteFormService,
    protected responsavelLegalService: ResponsavelLegalService,
    protected tagService: TagService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareResponsavelLegal = (o1: IResponsavelLegal | null, o2: IResponsavelLegal | null): boolean =>
    this.responsavelLegalService.compareResponsavelLegal(o1, o2);

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (cliente) {
        this.updateForm(cliente);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('testeJHipsterApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.clienteFormService.getCliente(this.editForm);
    if (cliente.id !== null) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cliente: ICliente): void {
    this.cliente = cliente;
    this.clienteFormService.resetForm(this.editForm, cliente);

    this.responsavelLegalsSharedCollection = this.responsavelLegalService.addResponsavelLegalToCollectionIfMissing<IResponsavelLegal>(
      this.responsavelLegalsSharedCollection,
      cliente.responsavelLegal
    );
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, ...(cliente.tags ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.responsavelLegalService
      .query()
      .pipe(map((res: HttpResponse<IResponsavelLegal[]>) => res.body ?? []))
      .pipe(
        map((responsavelLegals: IResponsavelLegal[]) =>
          this.responsavelLegalService.addResponsavelLegalToCollectionIfMissing<IResponsavelLegal>(
            responsavelLegals,
            this.cliente?.responsavelLegal
          )
        )
      )
      .subscribe((responsavelLegals: IResponsavelLegal[]) => (this.responsavelLegalsSharedCollection = responsavelLegals));

    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, ...(this.cliente?.tags ?? []))))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));
  }
}
