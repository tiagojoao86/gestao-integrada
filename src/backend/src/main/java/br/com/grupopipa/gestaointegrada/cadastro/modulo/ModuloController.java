package br.com.grupopipa.gestaointegrada.cadastro.modulo;

import static br.com.grupopipa.gestaointegrada.core.controller.Response.ok;

import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.controller.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.grupopipa.gestaointegrada.core.controller.BaseController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(ModuloConstants.R_MODULO)
public class ModuloController extends BaseController<ModuloDTO, ModuloGridDTO> {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
        this.business = moduloService;
    }

    @GetMapping
    public Response listAll() {
        List<ModuloDTO> list = moduloService.findAllSimple();
        return ok(list);
    }

    @GetMapping("/grouped")
    public Response listGrouped() {
        List<ModuloDTO> list = moduloService.findAllSimple();
        Map<String, List<ModuloDTO>> grouped = list.stream()
                .collect(Collectors.groupingBy(m -> m.getGrupoEnum() != null ? m.getGrupoEnum().name() : "UNDEFINED"));
        return ok(grouped);
    }

    @Override
    public Response save(ModuloDTO body) {
        throw new UnsupportedOperationException("Save not supported for Modulo");
    }

    @Override
    public Response delete(UUID id) {
        throw new UnsupportedOperationException("Delete not supported for Modulo");
    }

}
