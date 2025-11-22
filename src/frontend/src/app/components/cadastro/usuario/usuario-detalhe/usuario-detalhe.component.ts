import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { RouteConstants } from '../../../base/constants/route-constants';
import { UsuarioService } from '../usuario.service';
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
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { MessageService } from '../../../base/messages/messages.service';
import { UsuarioDTO } from '../model/usuario-dto';

@Component({
  selector: 'gi-usuario-detalhe',
  imports: [
    CommonModule,
    BaseComponent,
    IftaLabelModule,
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
  ],
  templateUrl: './usuario-detalhe.component.html',
  styleUrl: './usuario-detalhe.component.css',
  providers: [UsuarioService],
})
export class UsuarioDetalheComponent implements OnInit {
  form: FormGroup = new FormGroup([]);
  modoEdicao = false;
  usuario: UsuarioDTO = {} as UsuarioDTO;
  @Input() detailId: string | number | null = null;
  @Output() closeDetail = new EventEmitter<void>();

  private service: UsuarioService = inject(UsuarioService);
  private messages: MessageService = inject(MessageService);

  titulo = $localize`Usuário: `;

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
    } else {
      this.modoEdicao = true;
      this.service.findById(String(this.detailId!)).subscribe((response) => {
        this.usuario = response.body;
        this.titulo += this.usuario.nome;
        this.fillForm();
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

  salvar() {
    if (!this.form.valid) {
      this.messages.erro($localize`Existem campo inválidos.`);
      return;
    }

    this.usuario.nome = this.form.value.nome;
    this.usuario.login = this.form.value.login;
    this.usuario.senha = this.form.value.senha;

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
