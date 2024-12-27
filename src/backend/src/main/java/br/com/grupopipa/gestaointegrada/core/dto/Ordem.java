package br.com.grupopipa.gestaointegrada.core.dto;

import br.com.grupopipa.gestaointegrada.core.enums.OrdemDirecao;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Ordem {
    private OrdemDirecao direcao;
    private String propriedade;
}
