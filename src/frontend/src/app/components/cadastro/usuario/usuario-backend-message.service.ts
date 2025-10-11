import { Injectable } from "@angular/core";
import { AbstractTraslateBackendMessageService } from "../../../services/backend-messsages/abstract-translate-backend-message.service";

@Injectable({
  providedIn: 'root',
})
export class UsuarioBackendMessages extends AbstractTraslateBackendMessageService {
  messages(): { [key: string]: string } {
    return {
      'usuario.nome.notBlank': $localize`:@@usuario.nome.notBlank:O nome do usuário não pode ser em branco.`,
      'usuario.login.notBlank': $localize`:@@usuario.login.notBlank:O login não pode ser em branco.`,
      'usuario.senha.notBlank': $localize`:@@usuario.senha.notBlank:A senha não pode ser em branco.`,
      'usuario.login.unique': $localize`:@@usuario.login.unique:Este login já está em uso.`,
    };
  }
}