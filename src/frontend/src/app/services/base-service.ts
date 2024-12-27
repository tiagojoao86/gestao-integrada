import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RequisicaoPaginada } from "../model/requisicao-paginada";
import { Response } from "../model/response";

@Injectable()
export class BaseService<G, D> {

    private readonly R_QUERY: string = '/query';
    private readonly R_FIND_BY_ID: string = '/find-by-id';
    private readonly F_ID: string = 'id';
    
    urlBase: string = '/api/';

    constructor(private httpClient: HttpClient, private dominio: String) {}

    list(requisicao: RequisicaoPaginada): Observable<Response> {
        return this.httpClient.post<Response>(this.getUrl(this.R_QUERY), requisicao, { headers: this.getHeaders() });
    }

    save(dto: D): Observable<Response> {
        return this.httpClient.post<Response>(this.getUrl(), dto, { headers: this.getHeaders() });
    }

    findById(id: string): Observable<Response> {
        return this.httpClient.get<Response>(this.getUrl(this.R_FIND_BY_ID), { headers: this.getHeaders(), params: { F_ID: id} })
    }

    delete(id: string): Observable<Response> {
        return this.httpClient.delete<Response>(this.getUrl('/' + id), { headers: this.getHeaders() })
    }

    getUrl(contexto?: string): string {
        return this.urlBase + this.dominio + contexto;
    }

    getHeaders(): HttpHeaders {
        return new HttpHeaders({
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        });
    }

}