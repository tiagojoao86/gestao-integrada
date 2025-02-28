import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteConstants } from '../../../../constants/route-constants';
import { AppUserService } from '../../../../services/app-user.service';
import {
  RegisterActionToolbar,
  BaseComponent,
} from '../../../base/base.component';
import { CommonModule, Location } from '@angular/common';
import { IftaLabelModule } from 'primeng/iftalabel';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import {
  MessageService,
} from '../../../base/messages/messages.service';
import { messageServiceProvider } from '../../../base/messages/message.factory';
import { UserAppDTO } from '../../../../model/userapp-dto';

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
    AppUserService,
    { provide: MessageService, useFactory: messageServiceProvider },
  ],
})
export class UsuarioDetalheComponent {
  form: FormGroup = new FormGroup([]);
  modoEdicao: boolean = false;
  usuario: UserAppDTO = {} as UserAppDTO;

  titulo = 'Usuário: ';

  acoesTela: RegisterActionToolbar[] = [
    {
      action: () => {
        this.location.back();
      },
      icon: 'close',
      title: 'Cancelar',
    },
    {
      action: () => {
        this.salvar();
      },
      icon: 'save',
      title: 'Salvar',
    },
  ];

  constructor(
    private route: ActivatedRoute,
    private service: AppUserService,
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
        this.titulo += this.usuario.name;
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
    this.form.get('nome')?.setValue(this.usuario.name);
    this.form.get('login')?.setValue(this.usuario.username);
    this.form.get('senha')?.setValue(this.usuario.password);
  }

  salvar() {
    if (!this.form.valid) {
      this.messages.erro('Existem campo inválidos.');
      return;
    }

    this.usuario.name = this.form.value.nome;
    this.usuario.username = this.form.value.login;
    this.usuario.password = this.form.value.senha;

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
