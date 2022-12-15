import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ResponsavelLegalFormService, ResponsavelLegalFormGroup } from './responsavel-legal-form.service';
import { IResponsavelLegal } from '../responsavel-legal.model';
import { ResponsavelLegalService } from '../service/responsavel-legal.service';

@Component({
  selector: 'jhi-responsavel-legal-update',
  templateUrl: './responsavel-legal-update.component.html',
})
export class ResponsavelLegalUpdateComponent implements OnInit {
  isSaving = false;
  responsavelLegal: IResponsavelLegal | null = null;

  editForm: ResponsavelLegalFormGroup = this.responsavelLegalFormService.createResponsavelLegalFormGroup();

  constructor(
    protected responsavelLegalService: ResponsavelLegalService,
    protected responsavelLegalFormService: ResponsavelLegalFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsavelLegal }) => {
      this.responsavelLegal = responsavelLegal;
      if (responsavelLegal) {
        this.updateForm(responsavelLegal);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const responsavelLegal = this.responsavelLegalFormService.getResponsavelLegal(this.editForm);
    if (responsavelLegal.id !== null) {
      this.subscribeToSaveResponse(this.responsavelLegalService.update(responsavelLegal));
    } else {
      this.subscribeToSaveResponse(this.responsavelLegalService.create(responsavelLegal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResponsavelLegal>>): void {
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

  protected updateForm(responsavelLegal: IResponsavelLegal): void {
    this.responsavelLegal = responsavelLegal;
    this.responsavelLegalFormService.resetForm(this.editForm, responsavelLegal);
  }
}
