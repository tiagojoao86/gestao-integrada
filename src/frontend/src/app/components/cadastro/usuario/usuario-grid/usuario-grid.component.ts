import { Component, EventEmitter, Output } from '@angular/core';
import {
  RegisterActionToolbar,
  BaseComponent,
} from '../../../base/base.component';
import { UsuarioService } from '../../../../services/usuario.service';
import { Order, PageRequest } from '../../../../model/page-request';
import { UsuarioGridDTO } from '../../../../model/usuario-grid-dto';
import { CommonModule, DatePipe } from '@angular/common';
import {
  Action,
  DataSourceColumn,
  TableComponent,
} from '../../../base/table/table.component';
import {
  PaginationEvent,
  PaginatorComponent,
} from '../../../base/paginator/paginator.component';
import {
  FilterProperty,
  FiltroComponent,
  FilterType,
} from '../../../base/filter/filter.component';
import { FilterDTO, FilterLogicOperator } from '../../../../model/filter-dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuario-grid',
  imports: [
    BaseComponent,
    CommonModule,
    TableComponent,
    PaginatorComponent,
    FiltroComponent,
  ],
  providers: [UsuarioService, DatePipe],
  templateUrl: './usuario-grid.component.html',
  styleUrl: './usuario-grid.component.css',
})
export class UsuarioGridComponent {
  titulo: string = $localize `Cadastro de usuários`;

  @Output() openDetail = new EventEmitter<string>();

  itensPorPagina = PaginationEvent.DEFAULT_PAGE_SIZE;
  totalElements = 0;
  hideFilters = true;

  usuariosList: UsuarioGridDTO[] = [];

  colunas: DataSourceColumn[] = [
    {
      name: 'nome',
      label: $localize `Nome`,
      getValue: (element: UsuarioGridDTO) => {
        return element.nome;
      },
    },
    {
      name: 'login',
      label: $localize `Login`,
      getValue: (element: UsuarioGridDTO) => {
        return element.login;
      },
    },
    {
      name: 'createdAt',
      label: $localize `Criado em`,
      getValue: (element: UsuarioGridDTO) => {
        return this.datePipe.transform(element.createdAt, 'dd/MM/yyyy');
      },
    },
  ];

  acoesTabela: Action[] = [
    {
      icon: 'edit_note',
      action: (element: UsuarioGridDTO) => {
        this.openDetail.emit(element.id);
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

  acoesTela: RegisterActionToolbar[] = [
    {
      action: () => {
        this.refreshList();
      },
      icon: 'refresh',
      title: $localize `Atualizar`,
    },
    {
      action: () => {
        this.openDetail.emit('add');
      },
      icon: 'add',
      title: $localize `Adicionar`,
    },
    {
      action: () => {
        this.alternarMostrarFiltros();
      },
      icon: 'search',
      title: $localize `Pesquisar`,
      value: '0',
    },
  ];

  filtros: FilterProperty[] = [
    {
      property: 'login',
      label: $localize `Login`,
      filterType: FilterType.TEXTO,
    },
    {
      property: 'nome',
      label: $localize `Nome`,
      filterType: FilterType.TEXTO,
    },
    {
      property: 'createdAt',
      label: $localize `Criado em`,
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
