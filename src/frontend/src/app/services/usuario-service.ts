import { Injectable } from "@angular/core";
import { BaseService } from "./base-service";
import { HttpClient } from "@angular/common/http";
import { UsuarioDTO } from "../model/usuario-dto";
import { UsuarioGridDTO } from "../model/usuario-grid-dto";

@Injectable()
export class UsuarioService extends BaseService<UsuarioDTO, UsuarioGridDTO> {

    private static readonly USUARIO = 'usuario';

    constructor(httpClient: HttpClient) {
        super(httpClient, UsuarioService.USUARIO);
    }
}