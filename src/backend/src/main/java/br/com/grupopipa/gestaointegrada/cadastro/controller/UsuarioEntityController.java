package br.com.grupopipa.gestaointegrada.cadastro.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.UsuarioGridDTO;
import br.com.grupopipa.gestaointegrada.core.controller.BaseController;

import static br.com.grupopipa.gestaointegrada.cadastro.constants.Constants.R_USUARIO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(R_USUARIO)
public class UsuarioEntityController extends BaseController<UsuarioDTO, UsuarioGridDTO> {

}
