// login.component.ts
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../services/auth-service';
import { IftaLabelModule } from 'primeng/iftalabel';
import { FormUtilsService } from '../form-utils.service';
import { MessageService } from '../base/messages/messages.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    IftaLabelModule,
  ],
  providers: [MessageService, FormUtilsService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  form: FormGroup = new FormGroup({});
  formUtils;
  showError = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private messageService: MessageService,
    private formUtilsService: FormUtilsService
  ) {
    this.formUtils = this.formUtilsService;
  }

  ngOnInit(): void {
    const fb = new FormBuilder().nonNullable;
    this.form.addControl('login', fb.control(null, [Validators.required]));
    this.form.addControl('senha', fb.control(null, [Validators.required]));
  }

  login() {
    let login = this.form.value.login;
    let senha = this.form.value.senha;

    if (login && senha) {
      this.authService.login(login, senha).subscribe({
        next: () => {
          console.log('Login bem-sucedido');
        },
        error: (error) => {
          this.showError = true;
          if (error.status === 401) {
            this.errorMessage = 'Login ou senha inválidos';
          } else {
            this.errorMessage = 'Erro inesperado, tente novamente mais tarde';
          }
        },
      });
    }
  }

  closeMessage() {
    this.showError = false;
  }
}
