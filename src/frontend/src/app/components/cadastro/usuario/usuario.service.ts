import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UsuarioGridDTO } from './model/usuario-grid-dto';
import { UsuarioDTO } from './model/usuario-dto';
import { MessageService } from '../../base/messages/messages.service';
import { UsuarioBackendMessages } from './usuario-backend-message.service';
import { BaseService } from '../../base/base-service';


@Injectable()
export class UsuarioService extends BaseService<UsuarioGridDTO, UsuarioDTO> {
  private static readonly USUARIO = 'usuario';

  constructor(httpClient: HttpClient, messageService: MessageService, backendMessages: UsuarioBackendMessages) {
    super(httpClient, UsuarioService.USUARIO, messageService, backendMessages);
  }
}
