import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { RequisicaoPaginada } from '../model/requisicao-paginada';
import { Response } from '../model/response';
import { HttpConstants } from '../constants/http-constants';

@Injectable()
export class BaseService<G, D> {
  urlBase: string = '/api/';

  constructor(private httpClient: HttpClient, private dominio: String) {}

  list(requisicao: RequisicaoPaginada): Observable<Response> {
    return this.httpClient
      .post<Response>(this.getUrl(HttpConstants.R_QUERY), requisicao, {
        headers: this.getHeaders(),
      })
      .pipe(take(1));
  }

  save(dto: D): Observable<Response> {
    return this.httpClient
      .post<Response>(this.getUrl(), dto, { headers: this.getHeaders() })
      .pipe(take(1));
  }

  findById(id: string): Observable<Response> {
    return this.httpClient
      .get<Response>(this.getUrl(HttpConstants.R_FIND_BY_ID), {
        headers: this.getHeaders(),
        params: { id: id },
      })
      .pipe(take(1));
  }

  delete(id: string): Observable<Response> {
    return this.httpClient
      .delete<Response>(this.getUrl('/' + id), {
        headers: this.getHeaders(),
      })
      .pipe(take(1));
  }

  getUrl(contexto?: string): string {
    return this.urlBase + this.dominio + contexto;
  }

  getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    });
  }
}
