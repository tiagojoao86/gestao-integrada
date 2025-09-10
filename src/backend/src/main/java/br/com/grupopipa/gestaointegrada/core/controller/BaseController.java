package br.com.grupopipa.gestaointegrada.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import br.com.grupopipa.gestaointegrada.core.dto.OrderDTO;
import br.com.grupopipa.gestaointegrada.core.dto.PageRequest;
import br.com.grupopipa.gestaointegrada.core.exception.EntidadeNaoEncontradaException;
import br.com.grupopipa.gestaointegrada.core.service.CrudService;

import java.util.UUID;

import static br.com.grupopipa.gestaointegrada.core.constants.Constants.ENTITY_NOT_FOUND;
import static br.com.grupopipa.gestaointegrada.core.constants.Constants.F_ID;
import static br.com.grupopipa.gestaointegrada.core.constants.Constants.PV_ID;
import static br.com.grupopipa.gestaointegrada.core.constants.Constants.R_FIND_BY_ID;
import static br.com.grupopipa.gestaointegrada.core.constants.Constants.R_QUERY;
import static br.com.grupopipa.gestaointegrada.core.controller.Response.internalServerError;
import static br.com.grupopipa.gestaointegrada.core.controller.Response.notFoundException;
import static br.com.grupopipa.gestaointegrada.core.controller.Response.ok;

@Slf4j
public abstract class BaseController<D extends DTO, G extends GridDTO> {

    @Autowired
    protected CrudService<D, G> business;

    @PostMapping(R_QUERY)
    public Response list(@RequestBody PageRequest request) {
        try {
            Sort sort = Sort.by(request.getOrder().stream().map(OrderDTO::getOrder).toList());
            Pageable pageable = org.springframework.data.domain.PageRequest.of(request.getPage(), request.getSize(),
                    sort);
            return ok(business.list(request.getFilter(), pageable));
        } catch (Exception e) {
            return internalServerError(String.format("Causa: %s - Mensagem: %s", e.getCause(), e.getMessage()));
        }
    }

    @PostMapping
    public Response save(@RequestBody D body) {
        try {
            return ok(business.save(body));
        } catch (Exception e) {
            return buildErrorMessage(e);
        }
    }

    @GetMapping(R_FIND_BY_ID)
    public Response findById(@RequestParam(F_ID) UUID id) {
        try {
            return ok(business.findById(id));
        } catch (EntidadeNaoEncontradaException e) {
            return ok(String.format(ENTITY_NOT_FOUND, id));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @DeleteMapping(PV_ID)
    public Response delete(@PathVariable(F_ID) UUID id) {
        try {
            return ok(business.delete(id));
        } catch (EntidadeNaoEncontradaException e) {
            return notFoundException(e.getMessage());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    protected Response buildErrorMessage(Exception e) {
        String error = e.getMessage();
        return internalServerError(error);
    }

}
