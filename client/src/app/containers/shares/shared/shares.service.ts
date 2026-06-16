import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  MemberSharePortfolio,
  SaccoSharePortfolio,
  ShareAgeAnalysis,
  ShareProduct,
  ShareTransaction
} from './shares.models';
import { HttpClientHeadersService } from '../../../core/services';

@Injectable()
export class SharesService {

  constructor(private httpClient: HttpClient,
              private httpClientHeadersService: HttpClientHeadersService) {
  }

  getShareProducts(params?: any): Observable<any> {
    return this.httpClient.get(`${environment.API_ENDPOINT}share-products`, {
      params: this.httpClientHeadersService.buildQueryParams(params)
    });
  }

  getShareProduct(id: number): Observable<ShareProduct> {
    return this.httpClient.get<ShareProduct>(`${environment.API_ENDPOINT}share-products/${id}`);
  }

  saveShareProduct(payload: ShareProduct): Observable<ShareProduct> {
    return payload.id
      ? this.httpClient.put<ShareProduct>(`${environment.API_ENDPOINT}share-products/${payload.id}`, payload)
      : this.httpClient.post<ShareProduct>(`${environment.API_ENDPOINT}share-products`, payload);
  }

  activateShareProduct(id: number): Observable<ShareProduct> {
    return this.httpClient.post<ShareProduct>(`${environment.API_ENDPOINT}share-products/${id}/activate`, {});
  }

  deactivateShareProduct(id: number): Observable<ShareProduct> {
    return this.httpClient.post<ShareProduct>(`${environment.API_ENDPOINT}share-products/${id}/deactivate`, {});
  }

  purchaseShares(payload: any): Observable<ShareTransaction> {
    return this.httpClient.post<ShareTransaction>(`${environment.API_ENDPOINT}shares/purchases`, payload);
  }

  transferShares(payload: any): Observable<ShareTransaction> {
    return this.httpClient.post<ShareTransaction>(`${environment.API_ENDPOINT}shares/transfers`, payload);
  }

  getShareTransactions(params?: any): Observable<any> {
    return this.httpClient.get(`${environment.API_ENDPOINT}shares/transactions`, {
      params: this.httpClientHeadersService.buildQueryParams(params)
    });
  }

  getMemberLots(profileId: number, params?: any): Observable<any> {
    return this.httpClient.get(`${environment.API_ENDPOINT}shares/members/${profileId}/lots`, {
      params: this.httpClientHeadersService.buildQueryParams(params)
    });
  }

  getMemberPortfolio(profileId: number): Observable<MemberSharePortfolio> {
    return this.httpClient.get<MemberSharePortfolio>(`${environment.API_ENDPOINT}shares/members/${profileId}/portfolio`);
  }

  getSaccoPortfolio(params?: any): Observable<SaccoSharePortfolio> {
    return this.httpClient.get<SaccoSharePortfolio>(`${environment.API_ENDPOINT}shares/portfolio/sacco`, {
      params: this.httpClientHeadersService.buildQueryParams(params)
    });
  }

  getAgeAnalysis(params?: any): Observable<ShareAgeAnalysis> {
    return this.httpClient.get<ShareAgeAnalysis>(`${environment.API_ENDPOINT}shares/portfolio/age-analysis`, {
      params: this.httpClientHeadersService.buildQueryParams(params)
    });
  }
}
