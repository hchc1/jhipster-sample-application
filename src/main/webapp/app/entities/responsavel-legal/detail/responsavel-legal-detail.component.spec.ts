import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResponsavelLegalDetailComponent } from './responsavel-legal-detail.component';

describe('ResponsavelLegal Management Detail Component', () => {
  let comp: ResponsavelLegalDetailComponent;
  let fixture: ComponentFixture<ResponsavelLegalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResponsavelLegalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ responsavelLegal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResponsavelLegalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResponsavelLegalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load responsavelLegal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.responsavelLegal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
