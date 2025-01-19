export interface UsuarioDTO {
  id: string;
  nome: string;
  login: string;
  senha: string;
  criadoEm?: Date;
  atualizadoEm?: Date;
  criadoPor?: string;
  atualizadoPor?: string;
}

export class UsuarioDTOBuilder {
  usuarioDTO: UsuarioDTO = {} as UsuarioDTO;

  id(id: string): UsuarioDTOBuilder {
    this.usuarioDTO.id = id;
    return this;
  }

  nome(nome: string): UsuarioDTOBuilder {
    this.usuarioDTO.nome = nome;
    return this;
  }

  login(login: string): UsuarioDTOBuilder {
    this.usuarioDTO.login = login;
    return this;
  }

  senha(senha: string): UsuarioDTOBuilder {
    this.usuarioDTO.senha = senha;
    return this;
  }

  criadoEm(criadoEm: Date): UsuarioDTOBuilder {
    this.usuarioDTO.criadoEm = criadoEm;
    return this;
  }

  atualizadoEm(atualizadoEm: Date): UsuarioDTOBuilder {
    this.usuarioDTO.atualizadoEm = atualizadoEm;
    return this;
  }

  criadoPor(criadoPor: string): UsuarioDTOBuilder {
    this.usuarioDTO.criadoPor = criadoPor;
    return this;
  }

  atualizadoPor(atualizadoPor: string): UsuarioDTOBuilder {
    this.usuarioDTO.atualizadoPor = atualizadoPor;
    return this;
  }

  build(): UsuarioDTO {
    return this.usuarioDTO;
  }
}
