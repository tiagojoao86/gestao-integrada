package br.com.grupopipa.gestaointegrada.core.business;

import java.util.UUID;

import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import br.com.grupopipa.gestaointegrada.core.dto.RequisicaoPaginada;
import br.com.grupopipa.gestaointegrada.core.dto.RespostaPaginada;

public interface CrudBusiness<D extends DTO, G extends GridDTO> {

    public D save(D dto);

    public UUID delete(UUID id);

    public RespostaPaginada<G> list(RequisicaoPaginada requisicao);

    public D findById(UUID id);

}
