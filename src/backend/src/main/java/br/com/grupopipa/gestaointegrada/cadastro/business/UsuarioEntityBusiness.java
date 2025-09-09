package br.com.grupopipa.gestaointegrada.cadastro.business;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.business.CrudBusiness;

public interface UsuarioEntityBusiness extends CrudBusiness<UsuarioDTO, UsuarioGridDTO> {

    UsuarioDTO findUsuarioDTOByLogin(String login);
    
}
