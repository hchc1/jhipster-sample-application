import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResponsavelLegal } from '../responsavel-legal.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../responsavel-legal.test-samples';

import { ResponsavelLegalService } from './responsavel-legal.service';

const requireRestSample: IResponsavelLegal = {
  ...sampleWithRequiredData,
};

describe('ResponsavelLegal Service', () => {
  let service: ResponsavelLegalService;
  let httpMock: HttpTestingController;
  let expectedResult: IResponsavelLegal | IResponsavelLegal[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResponsavelLegalService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ResponsavelLegal', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const responsavelLegal = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(responsavelLegal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResponsavelLegal', () => {
      const responsavelLegal = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(responsavelLegal).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResponsavelLegal', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResponsavelLegal', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ResponsavelLegal', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addResponsavelLegalToCollectionIfMissing', () => {
      it('should add a ResponsavelLegal to an empty array', () => {
        const responsavelLegal: IResponsavelLegal = sampleWithRequiredData;
        expectedResult = service.addResponsavelLegalToCollectionIfMissing([], responsavelLegal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(responsavelLegal);
      });

      it('should not add a ResponsavelLegal to an array that contains it', () => {
        const responsavelLegal: IResponsavelLegal = sampleWithRequiredData;
        const responsavelLegalCollection: IResponsavelLegal[] = [
          {
            ...responsavelLegal,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResponsavelLegalToCollectionIfMissing(responsavelLegalCollection, responsavelLegal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResponsavelLegal to an array that doesn't contain it", () => {
        const responsavelLegal: IResponsavelLegal = sampleWithRequiredData;
        const responsavelLegalCollection: IResponsavelLegal[] = [sampleWithPartialData];
        expectedResult = service.addResponsavelLegalToCollectionIfMissing(responsavelLegalCollection, responsavelLegal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(responsavelLegal);
      });

      it('should add only unique ResponsavelLegal to an array', () => {
        const responsavelLegalArray: IResponsavelLegal[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const responsavelLegalCollection: IResponsavelLegal[] = [sampleWithRequiredData];
        expectedResult = service.addResponsavelLegalToCollectionIfMissing(responsavelLegalCollection, ...responsavelLegalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const responsavelLegal: IResponsavelLegal = sampleWithRequiredData;
        const responsavelLegal2: IResponsavelLegal = sampleWithPartialData;
        expectedResult = service.addResponsavelLegalToCollectionIfMissing([], responsavelLegal, responsavelLegal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(responsavelLegal);
        expect(expectedResult).toContain(responsavelLegal2);
      });

      it('should accept null and undefined values', () => {
        const responsavelLegal: IResponsavelLegal = sampleWithRequiredData;
        expectedResult = service.addResponsavelLegalToCollectionIfMissing([], null, responsavelLegal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(responsavelLegal);
      });

      it('should return initial array if no ResponsavelLegal is added', () => {
        const responsavelLegalCollection: IResponsavelLegal[] = [sampleWithRequiredData];
        expectedResult = service.addResponsavelLegalToCollectionIfMissing(responsavelLegalCollection, undefined, null);
        expect(expectedResult).toEqual(responsavelLegalCollection);
      });
    });

    describe('compareResponsavelLegal', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResponsavelLegal(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareResponsavelLegal(entity1, entity2);
        const compareResult2 = service.compareResponsavelLegal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareResponsavelLegal(entity1, entity2);
        const compareResult2 = service.compareResponsavelLegal(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareResponsavelLegal(entity1, entity2);
        const compareResult2 = service.compareResponsavelLegal(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
