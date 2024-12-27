package br.com.grupopipa.gestaointegrada.core.dto;

import br.com.grupopipa.gestaointegrada.core.enums.FiltroOperador;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FiltroItem {
    private String campo;
    private FiltroOperador operador;
    private String[] valores;
}