package br.com.grupopipa.gestaointegrada.core.business.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.grupopipa.gestaointegrada.core.business.CrudBusiness;
import br.com.grupopipa.gestaointegrada.core.dao.CrudDAO;
import br.com.grupopipa.gestaointegrada.core.dto.DTO;
import br.com.grupopipa.gestaointegrada.core.dto.Filtro;
import br.com.grupopipa.gestaointegrada.core.dto.GridDTO;
import br.com.grupopipa.gestaointegrada.core.dto.Ordem;
import br.com.grupopipa.gestaointegrada.core.dto.RequisicaoPaginada;
import br.com.grupopipa.gestaointegrada.core.dto.RespostaPaginada;

@Service
public abstract class CrudBusinessImpl<D extends DTO, G extends GridDTO> implements CrudBusiness<D, G> {

    @Autowired
    private CrudDAO<D, G> dao;

    public D save(D dto) {
        if (Objects.isNull(dto.getId())) {
            return dao.insert(dto);
        }

        return dao.update(dto);
    }

    public UUID delete(UUID id) {
        return dao.delete(id);
    }

    public RespostaPaginada<G> list(RequisicaoPaginada requisicao) {
        Filtro filtro = requisicao.getFiltro();
        Integer paginaNumero = requisicao.getPaginaNumero();
        Integer paginaTamanho = requisicao.getPaginaTamanho();
        List<Ordem> ordenacao = requisicao.getOrdenacao();

        G[] dados = dao.list(filtro, paginaTamanho, paginaNumero, ordenacao);

        Integer totalRegistros = dao.count(filtro);

        return RespostaPaginada
                .<G>builder()
                .dados(dados)
                .totalRegistros(totalRegistros)
                .paginaNumero(paginaNumero)
                .paginaTamanho(paginaTamanho)
                .build();
    }

    public D findById(UUID id) {
        return dao.findById(id);
    }

}
