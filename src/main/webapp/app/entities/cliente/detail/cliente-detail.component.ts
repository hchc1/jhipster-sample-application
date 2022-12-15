import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICliente } from '../cliente.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html',
})
export class ClienteDetailComponent implements OnInit {
  cliente: ICliente | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
