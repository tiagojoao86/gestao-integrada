import { Injectable } from '@angular/core';
import { BaseService } from './base-service';
import { HttpClient } from '@angular/common/http';
import { UsuarioDTO } from '../model/usuario-dto';
import { UsuarioGridDTO } from '../model/usuario-grid-dto';
import { UsuarioBackendMessages } from '../components/cadastro/usuario/usuario-backend-message.service';
import { MessageService } from '../components/base/messages/messages.service';

@Injectable()
export class UsuarioService extends BaseService<UsuarioGridDTO, UsuarioDTO> {
  private static readonly USUARIO = 'usuario';

  constructor(httpClient: HttpClient, messageService: MessageService, backendMessages: UsuarioBackendMessages) {
    super(httpClient, UsuarioService.USUARIO, messageService, backendMessages);
  }
}
