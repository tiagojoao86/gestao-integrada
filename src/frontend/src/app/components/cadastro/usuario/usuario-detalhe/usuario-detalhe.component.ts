import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { RouteConstants } from '../../../base/constants/route-constants';
import { UsuarioService } from '../usuario.service';
import { PerfilService } from '../../perfil/perfil.service';
import {
  RegisterActionToolbar,
  BaseComponent,
} from '../../../base/base.component';
import { CommonModule } from '@angular/common';
import { IftaLabelModule } from 'primeng/iftalabel';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  FormsModule,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { MessageService } from '../../../base/messages/messages.service';
import { UsuarioDTO } from '../model/usuario-dto';
import { PerfilDTO } from '../../perfil/model/perfil-dto';
import { PageRequest } from '../../../base/model/page-request';
import { FilterLogicOperator } from '../../../base/model/filter-dto';

@Component({
  selector: 'gi-usuario-detalhe',
  imports: [
    CommonModule,
    BaseComponent,
    IftaLabelModule,
    ReactiveFormsModule,
    FormsModule,
    InputTextModule,
    PasswordModule,
  ],
  templateUrl: './usuario-detalhe.component.html',
  styleUrl: './usuario-detalhe.component.css',
  providers: [UsuarioService, PerfilService],
})
export class UsuarioDetalheComponent implements OnInit {
  form: FormGroup = new FormGroup([]);
  modoEdicao = false;
  usuario: UsuarioDTO = {} as UsuarioDTO;
  @Input() detailId: string | number | null = null;
  @Output() closeDetail = new EventEmitter<void>();

  private service: UsuarioService = inject(UsuarioService);
  private perfilService: PerfilService = inject(PerfilService);
  private messages: MessageService = inject(MessageService);

  titulo = $localize`Usuário: `;

  // perfis
  allPerfis: PerfilDTO[] = [];
  availablePerfis: PerfilDTO[] = [];
  selectedPerfis: PerfilDTO[] = [];
  perfilFilter = '';

  acoesTela: RegisterActionToolbar[] = [
    {
      action: () => {
        this.goBackFn();
      },
      icon: 'close',
      title: $localize`Cancelar` + ' (esc)',
      shortcut: 'escape',
    },
    {
      action: () => {
        this.salvar();
      },
      icon: 'save',
      title: $localize`Salvar` + ' (enter)',
      shortcut: 'enter',
    },
  ];

  ngOnInit(): void {
    this.initForm();

    if (this.detailId === RouteConstants.P_ADD) {
      this.modoEdicao = false;
      this.titulo += $localize`Novo`;
      // no usuario yet, ensure lists will be initialized after loading perfis
      this.usuario = {} as UsuarioDTO;
      this.loadPerfisAndInitLists();
    } else {
      this.modoEdicao = true;
      this.service.findById(String(this.detailId!)).subscribe((response) => {
        this.usuario = response.body;
        this.titulo += this.usuario.nome;
        this.fillForm();
        this.loadPerfisAndInitLists();
      });
    }
  }

  initForm() {
    const fb = new FormBuilder().nonNullable;
    this.form.addControl('nome', fb.control(null));
    this.form.addControl('login', fb.control(null));
    this.form.addControl('senha', fb.control(null));
  }

  fillForm() {
    this.form.get('nome')?.setValue(this.usuario.nome);
    this.form.get('login')?.setValue(this.usuario.login);
    this.form.get('senha')?.setValue(this.usuario.senha);
  }

  adicionarPerfil(perfil: PerfilDTO) {
    if (!perfil) return;
    this.selectedPerfis.push(perfil);
    this.availablePerfis = this.availablePerfis.filter((p) => p.id !== perfil.id);
  }

  removerPerfil(perfil: PerfilDTO) {
    if (!perfil) return;
    this.availablePerfis.push(perfil);
    this.selectedPerfis = this.selectedPerfis.filter((p) => p.id !== perfil.id);
  }

  getAvailablePerfisFiltered(): PerfilDTO[] {
    const f = this.perfilFilter ? this.perfilFilter.toLowerCase() : '';
    if (!f) return this.availablePerfis;
    return this.availablePerfis.filter((p) => (p.nome || '').toLowerCase().includes(f));
  }

  private loadPerfisAndInitLists() {
    this.perfilService
      .list(new PageRequest({ filterLogicOperator: FilterLogicOperator.AND.getKey(), items: [] }, 9999, 0, []))
      .subscribe((r) => {
        this.allPerfis = r.body.content || [];
        this.selectedPerfis = this.usuario.perfis || [];
        this.availablePerfis = this.allPerfis.filter((p) => !this.selectedPerfis.some((sp) => sp.id === p.id));
      });
  }

  salvar() {
    if (!this.form.valid) {
      this.messages.erro($localize`Existem campo inválidos.`);
      return;
    }

    this.usuario.nome = this.form.value.nome;
    this.usuario.login = this.form.value.login;
    this.usuario.senha = this.form.value.senha;
    this.usuario.perfis = this.selectedPerfis;

    this.service.save(this.usuario, {
      onSuccess: (data: UsuarioDTO) => {
        this.usuario = data;
        this.messages.sucesso($localize`Usuário salvo com sucesso.`);
        this.goBackFn();
      },
    });
  }

  isControlInvalid(campo: string) {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const fc: AbstractControl<any, any> | null = this.form.get(campo);

    if (fc !== null && fc.invalid && (fc.touched || fc.dirty)) {
      return true;
    }

    return false;
  }

  goBackFn = () => {
    this.closeDetail.emit();
  };
}
