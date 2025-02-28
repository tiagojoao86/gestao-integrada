import { Component } from '@angular/core';
import {
  RegisterActionToolbar,
  BaseComponent,
} from '../../base/base.component';
import { AppUserService } from '../../../services/app-user.service';
import { Order, PageRequest } from '../../../model/page-request';
import { UserAppGridDTO } from '../../../model/userapp-grid-dto';
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
  providers: [AppUserService, DatePipe],
  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.css',
})
export class UsuariosComponent {
  titulo: string = 'Cadastro de usuários';

  itensPorPagina = PaginationEvent.DEFAULT_PAGE_SIZE;
  totalElements = 0;
  hideFilters = true;

  usuariosList: UserAppGridDTO[] = [];

  colunas: DataSourceColumn[] = [
    {
      name: 'name',
      label: 'Nome',
      getValue: (element: UserAppGridDTO) => {
        return element.name;
      },
    },
    {
      name: 'username',
      label: 'Login',
      getValue: (element: UserAppGridDTO) => {
        return element.username;
      },
    },
    {
      name: 'createdAt',
      label: 'Criado em',
      getValue: (element: UserAppGridDTO) => {
        return this.datePipe.transform(element.createdAt, 'dd/MM/yyyy');
      },
    },
  ];

  acoesTabela: Action[] = [
    {
      icon: 'edit_note',
      action: (element: UserAppGridDTO) => {
        this.router.navigate(['/cadastro/usuario/' + element.id]);
      },
    },
    {
      icon: 'delete',
      action: (element: UserAppGridDTO) => {
        this.service
          .delete(element.id)
          .subscribe((it) => this.listarUsuarios());
      },
    },
  ];

  acoesTela: RegisterActionToolbar[] = [
    {
      action: () => {
        this.refreshList();
      },
      icon: 'refresh',
      title: 'Atualizar',
    },
    {
      action: () => {
        this.router.navigate(['/cadastro/usuario/add']);
      },
      icon: 'add',
      title: 'Adicionar',
    },
    {
      action: () => {
        this.alternarMostrarFiltros();
      },
      icon: 'search',
      title: 'Pesquisar',
      value: '0',
    },
  ];

  filtros: FilterProperty[] = [
    {
      property: 'username',
      label: 'Login',
      filterType: FilterType.TEXTO,
    },
    {
      property: 'name',
      label: 'Nome',
      filterType: FilterType.TEXTO,
    },
    {
      property: 'createdAt',
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
    private service: AppUserService,
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
    let acao = this.acoesTela.filter((it) => it.icon === 'search');
    if (acao.length > 0) {
      if (filter) {
        acao[0].value = filter.items.length + '';
        return;
      }
      acao[0].value = '0';
    }
  }

  alternarMostrarFiltros() {
    this.hideFilters = !this.hideFilters;
  }

  refreshList() {
    this.listarUsuarios();
  }
}
