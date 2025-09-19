package br.com.grupopipa.gestaointegrada.cadastro.usuario;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.core.controller.BaseController;

import static br.com.grupopipa.gestaointegrada.cadastro.usuario.UsuarioConstants.R_USUARIO;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(R_USUARIO)
public class UsuarioController extends BaseController<UsuarioDTO, UsuarioGridDTO> {

}
