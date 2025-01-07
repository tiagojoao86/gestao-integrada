export interface Filtro {
  operadorLogico: FiltroOperadorLogico;
  items: FiltroItem[];
}

export interface FiltroItem {
  campo: string;
  operador: FiltroOperador;
  textos?: string[];
  datas?: Date[];
  datasHora?: Date[];
  numeros?: number[];
}

export class FiltroOperadorLogico {
  static readonly E: FiltroOperadorLogico = new FiltroOperadorLogico('E', 'E');
  static readonly OU: FiltroOperadorLogico = new FiltroOperadorLogico(
    'OU',
    'OU'
  );

  private label: string;
  private key: string;

  constructor(label: string, key: string) {
    this.label = label;
    this.key = key;
  }

  static getAll(): FiltroOperadorLogico[] {
    return [this.E, this.OU];
  }
}

export class FiltroOperador {
  static readonly IGUAL: FiltroOperador = new FiltroOperador('Igual', 'IGUAL');
  static readonly DIFERENTE: FiltroOperador = new FiltroOperador(
    'Diferente',
    'DIFERENTE'
  );
  static readonly MAIOR: FiltroOperador = new FiltroOperador('Maior', 'MAIOR');
  static readonly MENOR: FiltroOperador = new FiltroOperador('Menor', 'MENOR');
  static readonly MAIOR_IGUAL: FiltroOperador = new FiltroOperador(
    'Maior ou Igual',
    'MAIOR_IGUAL'
  );
  static readonly MENOR_IGUAL: FiltroOperador = new FiltroOperador(
    'Menor ou Igual',
    'MENOR_IGUAL'
  );
  static readonly CONTEM: FiltroOperador = new FiltroOperador(
    'Contém',
    'CONTEM'
  );
  static readonly NAO_CONTEM: FiltroOperador = new FiltroOperador(
    'Não Contém',
    'NAO_CONTEM'
  );
  static readonly IN: FiltroOperador = new FiltroOperador('Está em', 'IN');
  static readonly NOT_IN: FiltroOperador = new FiltroOperador(
    'Não está em',
    'NOT_IN'
  );
  static readonly ENTRE: FiltroOperador = new FiltroOperador('Entre', 'ENTRE');

  label: string;
  key: string;

  constructor(label: string, key: string) {
    this.label = label;
    this.key = key;
  }

  static getAll(): FiltroOperador[] {
    return [
      FiltroOperador.IGUAL,
      FiltroOperador.DIFERENTE,
      FiltroOperador.MAIOR,
      FiltroOperador.MENOR,
      FiltroOperador.MAIOR_IGUAL,
      FiltroOperador.MENOR_IGUAL,
      FiltroOperador.CONTEM,
      FiltroOperador.NAO_CONTEM,
      FiltroOperador.IN,
    ];
  }
}
