package br.com.grupopipa.gestaointegrada.cadastro.business;

import org.springframework.stereotype.Service;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.business.impl.CrudBusinessImpl;

@Service
public class UsuarioBusiness extends CrudBusinessImpl<UsuarioDTO, UsuarioGridDTO> {
}
