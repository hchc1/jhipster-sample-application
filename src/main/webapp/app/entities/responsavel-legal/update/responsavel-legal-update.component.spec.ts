import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ResponsavelLegalFormService } from './responsavel-legal-form.service';
import { ResponsavelLegalService } from '../service/responsavel-legal.service';
import { IResponsavelLegal } from '../responsavel-legal.model';

import { ResponsavelLegalUpdateComponent } from './responsavel-legal-update.component';

describe('ResponsavelLegal Management Update Component', () => {
  let comp: ResponsavelLegalUpdateComponent;
  let fixture: ComponentFixture<ResponsavelLegalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let responsavelLegalFormService: ResponsavelLegalFormService;
  let responsavelLegalService: ResponsavelLegalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ResponsavelLegalUpdateComponent],
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
      .overrideTemplate(ResponsavelLegalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResponsavelLegalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    responsavelLegalFormService = TestBed.inject(ResponsavelLegalFormService);
    responsavelLegalService = TestBed.inject(ResponsavelLegalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const responsavelLegal: IResponsavelLegal = { id: 456 };

      activatedRoute.data = of({ responsavelLegal });
      comp.ngOnInit();

      expect(comp.responsavelLegal).toEqual(responsavelLegal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavelLegal>>();
      const responsavelLegal = { id: 123 };
      jest.spyOn(responsavelLegalFormService, 'getResponsavelLegal').mockReturnValue(responsavelLegal);
      jest.spyOn(responsavelLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavelLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: responsavelLegal }));
      saveSubject.complete();

      // THEN
      expect(responsavelLegalFormService.getResponsavelLegal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(responsavelLegalService.update).toHaveBeenCalledWith(expect.objectContaining(responsavelLegal));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavelLegal>>();
      const responsavelLegal = { id: 123 };
      jest.spyOn(responsavelLegalFormService, 'getResponsavelLegal').mockReturnValue({ id: null });
      jest.spyOn(responsavelLegalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavelLegal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: responsavelLegal }));
      saveSubject.complete();

      // THEN
      expect(responsavelLegalFormService.getResponsavelLegal).toHaveBeenCalled();
      expect(responsavelLegalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResponsavelLegal>>();
      const responsavelLegal = { id: 123 };
      jest.spyOn(responsavelLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsavelLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(responsavelLegalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
