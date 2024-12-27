import { Filtro } from "./filtro";

export class RequisicaoPaginada {
    filtro: Filtro | null;
    paginaTamanho: number = 5;
    paginaNumero: number = 0;
    ordenacao: Ordem[] = [];

    constructor(filtro: Filtro | null, paginaTamanho: number, paginaNumero: number, ordenacao: Ordem[]) {
        this.filtro = filtro;
        this.paginaTamanho = paginaTamanho;
        this.paginaNumero = paginaNumero;
        this.ordenacao = ordenacao;
    }
    
}

export interface Ordem {
    direcao: OrdemDirecao;
    propriedade: string;
}

export enum OrdemDirecao {
    ASC,
    DESC
}