export interface Filtro {
    operadorLogico: FiltroOperadorLogico;
    items: FiltroItem[];
}

export interface FiltroItem {
    campo: string;
    operador: FiltroOperador;
    valores: string[];
}

export enum FiltroOperadorLogico {
    E,
    OU
}

export enum FiltroOperador {
    IGUAL,
    DIFERENTE,
    MAIOR,
    MENOR,
    MAIOR_IGUAL,
    MENOR_IGUAL,
    CONTEM,
    IN
}