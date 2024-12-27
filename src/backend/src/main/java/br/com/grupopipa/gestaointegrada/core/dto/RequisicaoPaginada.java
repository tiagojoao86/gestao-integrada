package br.com.grupopipa.gestaointegrada.core.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import br.com.grupopipa.gestaointegrada.core.enums.OrdemDirecao;
import lombok.Builder;

@Builder
public class RequisicaoPaginada {
    private Filtro filtro;
    private Integer paginaTamanho;
    private Integer paginaNumero;
    private List<Ordem> ordenacao;

    public PageRequest getPage() {
        Sort sort = buildSort();
        if (!ObjectUtils.isEmpty(ordenacao)) {
            return PageRequest.of(Optional.ofNullable(paginaNumero).orElse(0),
                    Optional.ofNullable(paginaTamanho).orElse(10),
                    sort);
        }
        return PageRequest.of(Optional.ofNullable(paginaNumero).orElse(0),
                Optional.ofNullable(paginaTamanho).orElse(10));
    }

    private Sort buildSort() {
        if (ObjectUtils.isEmpty(ordenacao)) {
            return null;
        }

        return Sort.by(ordenacao.stream().map(this::convertToSortOrder).toList());
    }

    private Sort.Order convertToSortOrder(Ordem item) {
        if (Sort.Direction.ASC.equals(item.getDirecao().getDirecao())) {
            return Sort.Order.asc(item.getPropriedade());
        }

        return Sort.Order.desc(item.getPropriedade());
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public Integer getPaginaTamanho() {
        if (Objects.isNull(paginaTamanho) || paginaTamanho > 50) {
            return 10;
        }
        return paginaTamanho;
    }

    public Integer getPaginaNumero() {
        if (Objects.isNull(paginaNumero) || paginaNumero.equals(0)) {
            return 1;
        }
        return paginaNumero;
    }

    public List<Ordem> getOrdenacao() {
        if (ObjectUtils.isEmpty(ordenacao)) {
            return Arrays.asList(Ordem.builder().propriedade("id").direcao(OrdemDirecao.ASC).build());
        }
        return ordenacao;
    }

}
