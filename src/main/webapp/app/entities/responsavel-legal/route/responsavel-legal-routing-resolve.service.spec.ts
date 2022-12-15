import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IResponsavelLegal } from '../responsavel-legal.model';
import { ResponsavelLegalService } from '../service/responsavel-legal.service';

import { ResponsavelLegalRoutingResolveService } from './responsavel-legal-routing-resolve.service';

describe('ResponsavelLegal routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ResponsavelLegalRoutingResolveService;
  let service: ResponsavelLegalService;
  let resultResponsavelLegal: IResponsavelLegal | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ResponsavelLegalRoutingResolveService);
    service = TestBed.inject(ResponsavelLegalService);
    resultResponsavelLegal = undefined;
  });

  describe('resolve', () => {
    it('should return IResponsavelLegal returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResponsavelLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultResponsavelLegal).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResponsavelLegal = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultResponsavelLegal).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IResponsavelLegal>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResponsavelLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultResponsavelLegal).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
