package br.com.grupopipa.gestaointegrada.core.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RespostaPaginada<T> {
    T[] dados;
    Integer paginaTamanho;
    Integer paginaNumero;
    Integer totalRegistros;
}
