import { Component } from '@angular/core';
import { AcaoToolbarCadastro, BaseComponent } from '../../base/base.component';
import { UsuarioService } from '../../../services/usuario-service';
import { Order, PageRequest } from '../../../model/page-request';
import { UsuarioGridDTO } from '../../../model/usuario-grid-dto';
import { CommonModule, DatePipe } from '@angular/common';
import {
  Action,
  DataSourceColumn,
  TableComponent,
} from '../../base/table/table.component';
import {
  PaginationEvent,
  PaginatorComponent,
} from '../../base/paginator/paginator.component';
import {
  FilterProperty,
  FiltroComponent,
  FilterType,
} from '../../base/filter/filter.component';
import { FilterDTO, FilterLogicOperator } from '../../../model/filter-dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuario',
  imports: [
    BaseComponent,
    CommonModule,
    TableComponent,
    PaginatorComponent,
    FiltroComponent,
  ],
  providers: [UsuarioService, DatePipe],
  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.css',
})
export class UsuariosComponent {
  titulo: string = 'Cadastro de usuários';

  itensPorPagina = PaginationEvent.DEFAULT_PAGE_SIZE;
  totalElements = 0;
  hideFilters = true;

  usuariosList: UsuarioGridDTO[] = [];

  colunas: DataSourceColumn[] = [
    {
      name: 'nome',
      label: 'Nome',
      getValue: (element: UsuarioGridDTO) => {
        return element.nome;
      },
    },
    {
      name: 'login',
      label: 'Login',
      getValue: (element: UsuarioGridDTO) => {
        return element.login;
      },
    },
    {
      name: 'criadoEm',
      label: 'Criado em',
      getValue: (element: UsuarioGridDTO) => {
        return this.datePipe.transform(element.criadoEm, 'dd/MM/yyyy');
      },
    },
  ];

  acoesTabela: Action[] = [
    {
      icon: 'edit_note',
      action: (element: UsuarioGridDTO) => {
        this.router.navigate(['/cadastro/usuario/' + element.id]);
      },
    },
    {
      icon: 'delete',
      action: (element: UsuarioGridDTO) => {
        this.service
          .delete(element.id)
          .subscribe((it) => this.listarUsuarios());
      },
    },
  ];

  acoesTela: AcaoToolbarCadastro[] = [
    {
      acao: () => {
        this.router.navigate(['/cadastro/usuario/add']);
      },
      icone: 'add',
      titulo: 'Adicionar',
    },
    {
      acao: () => {
        this.alternarMostrarFiltros();
      },
      icone: 'search',
      titulo: 'Pesquisar',
      valor: '0',
    },
  ];

  filtros: FilterProperty[] = [
    {
      property: 'login',
      label: 'Login',
      filterType: FilterType.TEXTO,
    },
    {
      property: 'nome',
      label: 'Nome',
      filterType: FilterType.TEXTO,
    },
    {
      property: 'criadoEm',
      label: 'Criado em',
      filterType: FilterType.DATA,
    },
  ];

  request = new PageRequest(
    { filterLogicOperator: FilterLogicOperator.AND.getKey(), items: [] },
    this.itensPorPagina,
    0,
    []
  );

  constructor(
    private service: UsuarioService,
    private datePipe: DatePipe,
    private router: Router
  ) {
    this.listarUsuarios();
  }

  listarUsuarios() {
    this.service.list(this.request).subscribe((response) => {
      if (response.body) {
        this.usuariosList = response.body.content;
        this.totalElements = response.body.totalElements;
      }
    });
  }

  ordenar(order: Order[]) {
    this.request.order = order;
    this.listarUsuarios();
  }

  paginar(page: PaginationEvent) {
    this.request.page = page.pageNumber;
    this.request.size = page.itemsPerPage;

    this.listarUsuarios();
  }

  filtrar(filter: FilterDTO) {
    this.request.filter = filter;
    this.listarUsuarios();
    this.ajustaBadgePesquisa(filter);
  }

  cancelar() {
    this.alternarMostrarFiltros();
  }

  ajustaBadgePesquisa(filter: FilterDTO) {
    let acao = this.acoesTela.filter((it) => it.icone === 'search');
    if (acao.length > 0) {
      if (filter) {
        acao[0].valor = filter.items.length + '';
        return;
      }
      acao[0].valor = '0';
    }
  }

  alternarMostrarFiltros() {
    this.hideFilters = !this.hideFilters;
  }
}
