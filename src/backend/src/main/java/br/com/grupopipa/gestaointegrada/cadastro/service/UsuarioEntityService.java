package br.com.grupopipa.gestaointegrada.cadastro.service;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.service.CrudService;

public interface UsuarioEntityService extends CrudService<UsuarioDTO, UsuarioGridDTO> {

    UsuarioDTO findUsuarioDTOByLogin(String login);
    
}
