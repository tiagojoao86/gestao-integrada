package br.com.grupopipa.gestaointegrada.cadastro.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.rest.BaseRest;

import static br.com.grupopipa.gestaointegrada.cadastro.constants.Constants.R_USUARIO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(R_USUARIO)
public class UsuarioEntityRest extends BaseRest<UsuarioDTO, UsuarioGridDTO> {

}
