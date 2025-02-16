package br.com.grupopipa.gestaointegrada.cadastro.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserDTO;
import br.com.grupopipa.gestaointegrada.cadastro.dto.usuario.AppUserGridDTO;
import br.com.grupopipa.gestaointegrada.core.rest.BaseRest;

import static br.com.grupopipa.gestaointegrada.cadastro.constants.Constants.R_APP_USER;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(R_APP_USER)
public class AppUserRest extends BaseRest<AppUserDTO, AppUserGridDTO> {

}
