import { Component } from "@angular/core";
import { BaseComponent } from "../base/base.component";
import { CommonModule } from "@angular/common";
import { GrupoRecurso, RecursoGrupoComponent } from "../base/menu/recurso-grupo/recurso-grupo.component";

@Component({
    selector: 'cadastro',
    imports: [BaseComponent, CommonModule, RecursoGrupoComponent],
    templateUrl: './cadastro.component.html',
    styleUrl: './cadastro.component.css'
})
export class CadastroComponent {

    titulo: string = $localize `Cadastros`;

    recursos: GrupoRecurso[] = [
        {nome: $localize `Geral`, recursos: [{nome: $localize `Usuários`, icone: 'person', url: '/cadastro/usuario'}]}
    ];
    
}