package br.com.grupopipa.gestaointegrada.core.dto;

import java.util.Objects;

import br.com.grupopipa.gestaointegrada.core.enums.FiltroOperadorLogico;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Filtro {
    private FiltroOperadorLogico operadorLogico;
    private FiltroItem[] items;

    public boolean isFiltroEmpty() {
        return Objects.isNull(items) || items.length == 0;
    }
}
