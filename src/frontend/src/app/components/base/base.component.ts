import { CommonModule } from '@angular/common';
import { Component, Input, LOCALE_ID, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Location } from '@angular/common'

@Component({
    selector: 'app-base',
    imports: [MatToolbarModule, MatButtonModule, MatIconModule, CommonModule],
    templateUrl: './base.component.html',
    styleUrl: './base.component.css'
})
export class BaseComponent {
constructor(private location: Location) {}

  @Input('titulo') titulo: string = 'Título';
  @Input('acoes') acoes: AcaoToolbarCadastro[] = []; 
  @Input('ocultarFooter') ocultarFooter: boolean = false;
  @Input('ocultarToolbar') ocultarToolbar: boolean = false;

  voltar() {
    this.location.back();
  }

}

export interface AcaoToolbarCadastro {
  acao: Function;
  icone: string;
}