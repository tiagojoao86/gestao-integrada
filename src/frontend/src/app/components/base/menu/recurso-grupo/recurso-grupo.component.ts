import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { Recurso, RecursoComponent } from '../recurso/recurso.component';

@Component({
    selector: 'recurso-grupo',
    imports: [MatIcon, CommonModule, RecursoComponent],
    templateUrl: './recurso-grupo.component.html',
    styleUrl: './recurso-grupo.component.css'
})
export class RecursoGrupoComponent {
  
  @Input('recursoGrupo') recursoGrupo: GrupoRecurso | undefined;

  constructor() { }

}

export interface GrupoRecurso {
    nome: string;
    recursos: Recurso[];
}
