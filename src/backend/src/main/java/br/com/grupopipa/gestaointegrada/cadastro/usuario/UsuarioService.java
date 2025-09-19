package br.com.grupopipa.gestaointegrada.cadastro.usuario;

import br.com.grupopipa.gestaointegrada.core.service.CrudService;

public interface UsuarioService extends CrudService<UsuarioDTO, UsuarioGridDTO> {

    UsuarioDTO findUsuarioDTOByLogin(String login);
    
}
