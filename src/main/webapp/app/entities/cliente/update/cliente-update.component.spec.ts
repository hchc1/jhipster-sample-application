import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClienteFormService } from './cliente-form.service';
import { ClienteService } from '../service/cliente.service';
import { ICliente } from '../cliente.model';
import { IResponsavelLegal } from 'app/entities/responsavel-legal/responsavel-legal.model';
import { ResponsavelLegalService } from 'app/entities/responsavel-legal/service/responsavel-legal.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Cliente Management Update Component', () => {
  let comp: ClienteUpdateComponent;
  let fixture: ComponentFixture<ClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteFormService: ClienteFormService;
  let clienteService: ClienteService;
  let responsavelLegalService: ResponsavelLegalService;
  let tagService: TagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClienteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteFormService = TestBed.inject(ClienteFormService);
    clienteService = TestBed.inject(ClienteService);
    responsavelLegalService = TestBed.inject(ResponsavelLegalService);
    tagService = TestBed.inject(TagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ResponsavelLegal query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const responsavelLegal: IResponsavelLegal = { id: 99997 };
      cliente.responsavelLegal = responsavelLegal;

      const responsavelLegalCollection: IResponsavelLegal[] = [{ id: 32609 }];
      jest.spyOn(responsavelLegalService, 'query').mockReturnValue(of(new HttpResponse({ body: responsavelLegalCollection })));
      const additionalResponsavelLegals = [responsavelLegal];
      const expectedCollection: IResponsavelLegal[] = [...additionalResponsavelLegals, ...responsavelLegalCollection];
      jest.spyOn(responsavelLegalService, 'addResponsavelLegalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(responsavelLegalService.query).toHaveBeenCalled();
      expect(responsavelLegalService.addResponsavelLegalToCollectionIfMissing).toHaveBeenCalledWith(
        responsavelLegalCollection,
        ...additionalResponsavelLegals.map(expect.objectContaining)
      );
      expect(comp.responsavelLegalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tag query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const tags: ITag[] = [{ id: 56012 }];
      cliente.tags = tags;

      const tagCollection: ITag[] = [{ id: 75031 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [...tags];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags.map(expect.objectContaining));
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cliente: ICliente = { id: 456 };
      const responsavelLegal: IResponsavelLegal = { id: 82095 };
      cliente.responsavelLegal = responsavelLegal;
      const tags: ITag = { id: 35055 };
      cliente.tags = [tags];

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(comp.responsavelLegalsSharedCollection).toContain(responsavelLegal);
      expect(comp.tagsSharedCollection).toContain(tags);
      expect(comp.cliente).toEqual(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue(cliente);
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteService.update).toHaveBeenCalledWith(expect.objectContaining(cliente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue({ id: null });
      jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(clienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareResponsavelLegal', () => {
      it('Should forward to responsavelLegalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(responsavelLegalService, 'compareResponsavelLegal');
        comp.compareResponsavelLegal(entity, entity2);
        expect(responsavelLegalService.compareResponsavelLegal).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTag', () => {
      it('Should forward to tagService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tagService, 'compareTag');
        comp.compareTag(entity, entity2);
        expect(tagService.compareTag).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
