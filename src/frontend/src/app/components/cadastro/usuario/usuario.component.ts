import { Component } from '@angular/core';
import { AcaoToolbarCadastro, BaseComponent } from '../../base/base.component';
import { UsuarioService } from '../../../services/usuario-service';
import { Ordem, RequisicaoPaginada } from '../../../model/requisicao-paginada';
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
  FiltroCampo,
  FiltroComponent,
  TipoCampoFiltro,
} from '../../base/filtro/filtro.component';
import { Filtro } from '../../../model/filtro';
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
  totalRegistros = 0;
  ocultarFiltros = true;

  listaUsuarios: UsuarioGridDTO[] = [];

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

  filtros: FiltroCampo[] = [
    {
      nome: 'login',
      label: 'Login',
      tipoCampoFiltro: TipoCampoFiltro.TEXTO,
    },
    {
      nome: 'nome',
      label: 'Nome',
      tipoCampoFiltro: TipoCampoFiltro.TEXTO,
    },
    {
      nome: 'criadoEm',
      label: 'Criado em',
      tipoCampoFiltro: TipoCampoFiltro.DATA,
    },
  ];

  requisicao = new RequisicaoPaginada(null, this.itensPorPagina, 1, []);

  constructor(
    private service: UsuarioService,
    private datePipe: DatePipe,
    private router: Router
  ) {
    this.listarUsuarios();
  }

  listarUsuarios() {
    this.service.list(this.requisicao).subscribe((response) => {
      if (response.body) {
        this.listaUsuarios = response.body.dados;
        this.totalRegistros = response.body.totalRegistros;
      }
    });
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

  filtrar(filtro: Filtro) {
    this.requisicao.filtro = filtro;
    this.listarUsuarios();
    this.ajustaBadgePesquisa(filtro);
  }

  cancelar() {
    this.alternarMostrarFiltros();
  }

  ajustaBadgePesquisa(filtro: Filtro) {
    let acao = this.acoesTela.filter((it) => it.icone === 'search');
    if (acao.length > 0) {
      if (filtro) {
        acao[0].valor = filtro.items.length + '';
        return;
      }
      acao[0].valor = '0';
    }
  }

  alternarMostrarFiltros() {
    this.ocultarFiltros = !this.ocultarFiltros;
  }
}
