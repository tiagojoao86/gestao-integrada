package br.com.grupopipa.gestaointegrada.core.dao;

import java.util.List;
import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.Filtro;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import br.com.grupopipa.gestaointegrada.core.dto.Ordem;

public interface CrudDAO<D extends DTO, G extends GridDTO> {

    D insert(D dto);

    D update(D dto);

    UUID delete(UUID id);

    G[] list(Filtro filtro, Integer paginaTamanho, Integer paginaNumero, List<Ordem> ordenacao);

    Integer count(Filtro filtro);

    D findById(UUID id);

}
