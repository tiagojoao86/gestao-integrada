import { Component} from '@angular/core';
import { BaseComponent } from '../../base/base.component';
import { UsuarioService } from '../../../services/usuario-service';
import { Ordem, RequisicaoPaginada } from '../../../model/requisicao-paginada';
import { UsuarioGridDTO } from '../../../model/usuario-grid-dto';
import { CommonModule, DatePipe} from '@angular/common';
import { Action, DataSourceColumn, TableComponent } from '../../base/table/table.component';
import { PaginationEvent, PaginatorComponent } from '../../base/paginator/paginator.component';


@Component({
    selector: 'app-usuario',
    imports: [BaseComponent, CommonModule, TableComponent, PaginatorComponent],
    providers: [UsuarioService, DatePipe],
    templateUrl: './usuario.component.html',
    styleUrl: './usuario.component.css'
})
export class UsuariosComponent {

  titulo: string = 'Cadastro de usuários';

  itensPorPagina = PaginationEvent.DEFAULT_PAGE_SIZE;
  totalRegistros = 0;

  listaUsuarios: UsuarioGridDTO[] = [];

  colunas: DataSourceColumn[] = [
    {
      name: 'nome',
      label: 'Nome',
      getValue: (element: UsuarioGridDTO) => { return element.nome }
    },
    {
      name: 'login',
      label: 'Login',
      getValue: (element: UsuarioGridDTO) => { return element.login }
    },
    {
      name: 'criadoEm',
      label: 'Criado em',
      getValue: (element: UsuarioGridDTO) => {
        return this.datePipe.transform(element.criadoEm, 'dd/MM/yyyy')
      }
    }
  ];

  acoes: Action[] = [
    {
      icon: 'edit_note',
      action: (element: UsuarioGridDTO) => { console.log(element) }
    },
    {
      icon: 'delete',
      action: (element: UsuarioGridDTO) => {
        this.service.delete(element.id).subscribe(it =>
          this.listarUsuarios()
        );
        
      }
    },
  ];

  requisicao = new RequisicaoPaginada(null, this.itensPorPagina, 1, []);

  constructor(private service: UsuarioService, private datePipe: DatePipe) {
    this.listarUsuarios();
  }

  listarUsuarios() {    
    this.service.list(this.requisicao).subscribe(response => {      
      if (response.body) {
        this.listaUsuarios = response.body.dados;
        this.totalRegistros = response.body.totalRegistros;
      }      
    })
  }

  ordenar(ordenacao: Ordem[]) {
    this.requisicao.ordenacao = ordenacao;
    this.listarUsuarios();
  }

  paginar(paginacao: PaginationEvent) {
    this.requisicao.paginaNumero = paginacao.pageNumber;
    this.requisicao.paginaTamanho = paginacao.itemsPerPage;
    
    this.listarUsuarios();
  }

}
