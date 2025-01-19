import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteConstants } from '../../../../constants/route-constants';
import { UsuarioService } from '../../../../services/usuario-service';
import { UsuarioDTO } from '../../../../model/usuario-dto';
import {
  AcaoToolbarCadastro,
  BaseComponent,
} from '../../../base/base.component';
import { CommonModule, Location } from '@angular/common';
import { IftaLabelModule } from 'primeng/iftalabel';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import {
  MessageService,
  MessageType,
} from '../../../base/messages/messages.service';
import { messageServiceProvider } from '../../../base/messages/message.factory';

@Component({
  selector: 'app-usuario-detalhe',
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
  providers: [
    UsuarioService,
    { provide: MessageService, useFactory: messageServiceProvider },
  ],
})
export class UsuarioDetalheComponent {
  form: FormGroup = new FormGroup([]);
  modoEdicao: boolean = false;
  usuario: UsuarioDTO = {} as UsuarioDTO;

  titulo = 'Usuário: ';

  acoesTela: AcaoToolbarCadastro[] = [
    {
      acao: () => {
        this.location.back();
      },
      icone: 'close',
      titulo: 'Cancelar',
    },
    {
      acao: () => {
        this.salvar();
      },
      icone: 'save',
      titulo: 'Salvar',
    },
  ];

  constructor(
    private route: ActivatedRoute,
    private service: UsuarioService,
    private location: Location,
    private messages: MessageService
  ) {
    this.initForm();
    const id = this.route.snapshot.paramMap.get('id');

    if (id === RouteConstants.P_ADD) {
      this.modoEdicao = false;
      this.titulo += 'Novo';
    } else {
      this.modoEdicao = true;
      this.service.findById(id!).subscribe((response) => {
        this.usuario = response.body;
        this.titulo += this.usuario.nome;
        this.fillForm();
      });
    }
  }

  initForm() {
    const fb = new FormBuilder().nonNullable;
    this.form.addControl('nome', fb.control(null, [Validators.required]));
    this.form.addControl('login', fb.control(null, [Validators.required]));
    this.form.addControl('senha', fb.control(null));
  }

  fillForm() {
    this.form.get('nome')?.setValue(this.usuario.nome);
    this.form.get('login')?.setValue(this.usuario.login);
    this.form.get('senha')?.setValue(this.usuario.senha);
  }

  salvar() {
    if (!this.form.valid) {
      this.messages.erro('Existem campo inválidos.');
      return;
    }

    this.usuario.nome = this.form.value.nome;
    this.usuario.login = this.form.value.login;
    this.usuario.senha = this.form.value.senha;

    this.service.save(this.usuario).subscribe((response) => {
      this.usuario = response.body;
      this.messages.sucesso('Usuário salvo com sucesso.');
      this.location.back();
    });
  }

  isControlInvalid(campo: string) {
    const fc: AbstractControl<any, any> | null = this.form.get(campo);

    if (fc !== null && fc.invalid && (fc.touched || fc.dirty)) {
      return true;
    }

    return false;
  }
}
