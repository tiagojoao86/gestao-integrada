import { CommonModule } from '@angular/common';
import { Component, Input} from '@angular/core';
import { Location } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Toolbar } from 'primeng/toolbar';

@Component({
    selector: 'app-base',
    imports: [CommonModule, ButtonModule, Toolbar],
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