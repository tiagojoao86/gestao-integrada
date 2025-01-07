import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
  Filtro,
  FiltroItem,
  FiltroOperador,
  FiltroOperadorLogico,
} from '../../../model/filtro';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { DatePicker } from 'primeng/datepicker';

@Component({
  selector: 'filtro-component',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    SelectModule,
    DatePicker,
  ],
  templateUrl: './filtro.component.html',
  styleUrl: './filtro.component.css',
})
export class FiltroComponent implements OnInit {
  readonly TipoCampoFiltro = TipoCampoFiltro;

  @Input('filtros') filtros: FiltroCampo[] = [];
  filtrosSelecionados: FiltroCampo[] = [];
  @Output('filtrar') filtrar: EventEmitter<Filtro> = new EventEmitter();
  @Output('cancelar') cancelar: EventEmitter<boolean> = new EventEmitter();

  operacoes: FiltroOperador[] = FiltroOperador.getAll();
  operadoresLogicos: FiltroOperadorLogico[] = FiltroOperadorLogico.getAll();

  seletorForm: FormGroup = new FormGroup([]);
  form: FormGroup = new FormGroup([]);

  ngOnInit(): void {
    this.seletorForm.addControl('campo', new FormControl());
    this.seletorForm.addControl(
      'operadorLogico',
      new FormControl(FiltroOperadorLogico.E)
    );
  }

  getOperacoes(tipo: TipoCampoFiltro): FiltroOperador[] {
    if (TipoCampoFiltro.TEXTO === tipo) {
      return [
        FiltroOperador.CONTEM,
        FiltroOperador.NAO_CONTEM,
        FiltroOperador.DIFERENTE,
        FiltroOperador.IGUAL,
      ];
    }
    if (TipoCampoFiltro.DATA === tipo || TipoCampoFiltro.NUMERO === tipo) {
      return [
        FiltroOperador.DIFERENTE,
        FiltroOperador.IGUAL,
        FiltroOperador.MAIOR,
        FiltroOperador.MAIOR_IGUAL,
        FiltroOperador.MENOR,
        FiltroOperador.MENOR_IGUAL,
        FiltroOperador.ENTRE,
      ];
    }
    if (TipoCampoFiltro.SELECAO === tipo) {
      return [
        FiltroOperador.DIFERENTE,
        FiltroOperador.IGUAL,
        FiltroOperador.MAIOR,
        FiltroOperador.MAIOR_IGUAL,
        FiltroOperador.MENOR,
        FiltroOperador.MENOR_IGUAL,
      ];
    }
    if (TipoCampoFiltro.MULTI_SELECAO === tipo) {
      return [FiltroOperador.IN, FiltroOperador.NOT_IN];
    }

    return this.operacoes;
  }

  adicionarFiltro(campo: FiltroCampo) {
    if (this.filtrosSelecionados.indexOf(campo) === -1) {
      this.filtrosSelecionados.push(campo);
      this.filtros.forEach((filtro) => {
        if (filtro.nome === campo.nome) {
          this.form.addControl(filtro.nome, new FormControl());
          this.form.addControl(
            filtro.nome + '_operacao',
            this.buildDefaultOperation(campo.tipoCampoFiltro)
          );
        }
      });
    }
  }

  buildDefaultOperation(tipo: TipoCampoFiltro): FormControl {
    if (TipoCampoFiltro.TEXTO === tipo) {
      return new FormControl(FiltroOperador.CONTEM);
    }

    if (TipoCampoFiltro.DATA === tipo || TipoCampoFiltro.NUMERO === tipo) {
      return new FormControl(FiltroOperador.IGUAL);
    }

    if (
      TipoCampoFiltro.SELECAO === tipo ||
      TipoCampoFiltro.MULTI_SELECAO === tipo
    ) {
      return new FormControl(FiltroOperador.IN);
    }

    return new FormControl();
  }

  removerFiltro(campo: FiltroCampo) {
    let index = this.filtrosSelecionados.indexOf(campo);
    if (index !== -1) {
      this.filtrosSelecionados.splice(index, 1);
      this.onFiltrar();
    }
  }

  onFiltrar() {
    if (this.filtrosSelecionados.length === 0) {
      this.filtrar.emit();
      return;
    }

    let itens: FiltroItem[] = [];
    this.filtrosSelecionados.forEach((selecionado) => {
      let operador = this.form
        .get(selecionado.nome + '_operacao')
        ?.getRawValue().key;
      let valores = this.form.get(selecionado.nome)?.getRawValue();

      if (selecionado.tipoCampoFiltro === TipoCampoFiltro.DATA) {
        itens.push({
          campo: selecionado.nome,
          operador: operador,
          datas: [valores ? valores : ''],
        });
      }

      if (selecionado.tipoCampoFiltro === TipoCampoFiltro.TEXTO) {
        itens.push({
          campo: selecionado.nome,
          operador: operador,
          textos: [valores ? valores + '' : ''],
        });
      }
    });
    this.filtrar.emit({
      items: itens,
      operadorLogico: this.seletorForm.get('operadorLogico')?.getRawValue().key,
    });
  }

  onCancelar() {
    this.cancelar.emit();
  }
}

export interface FiltroCampo {
  label: string;
  nome: string;
  tipoCampoFiltro: TipoCampoFiltro;
  valores?: FiltroSelecaoValores[];
}

export enum TipoCampoFiltro {
  TEXTO,
  MULTI_SELECAO,
  SELECAO,
  NUMERO,
  DATA,
}

export interface FiltroSelecaoValores {
  chave: string;
  label: string;
}

export interface FiltroCamposSelecao {
  nome: string;
  label: string;
}
