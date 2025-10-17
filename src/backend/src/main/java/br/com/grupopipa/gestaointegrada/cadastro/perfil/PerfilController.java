package br.com.grupopipa.gestaointegrada.cadastro.perfil;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.core.controller.BaseController;

import static br.com.grupopipa.gestaointegrada.cadastro.perfil.PerfilConstants.R_PERFIL;;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(R_PERFIL)
public class PerfilController extends BaseController<PerfilDTO, PerfilGridDTO> {

}
