package br.com.grupopipa.gestaointegrada.cadastro.dao;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.dao.CrudDAO;

public interface UsuarioDAO extends CrudDAO<UsuarioDTO, UsuarioGridDTO> {
    UsuarioDTO findByNome(String nome);
}
