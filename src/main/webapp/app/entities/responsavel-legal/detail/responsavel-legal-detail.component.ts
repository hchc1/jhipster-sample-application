import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResponsavelLegal } from '../responsavel-legal.model';

@Component({
  selector: 'jhi-responsavel-legal-detail',
  templateUrl: './responsavel-legal-detail.component.html',
})
export class ResponsavelLegalDetailComponent implements OnInit {
  responsavelLegal: IResponsavelLegal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsavelLegal }) => {
      this.responsavelLegal = responsavelLegal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
