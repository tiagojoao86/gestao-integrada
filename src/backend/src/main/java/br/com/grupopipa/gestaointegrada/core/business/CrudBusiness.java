package br.com.grupopipa.gestaointegrada.core.business;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.PageDTO;
import br.com.grupopipa.gestaointegrada.core.dto.FilterDTO;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;

public interface CrudBusiness<D extends DTO, G extends GridDTO> {

    public D save(D dto);

    public UUID delete(UUID id);

    public PageDTO<G> list(FilterDTO filter, Pageable pageable);

    public D findById(UUID id);

}