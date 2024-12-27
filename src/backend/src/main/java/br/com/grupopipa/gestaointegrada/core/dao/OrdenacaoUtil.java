package br.com.grupopipa.gestaointegrada.core.dao;

import java.util.Objects;

import org.jooq.SortField;
import org.jooq.impl.DSL;

import br.com.grupopipa.gestaointegrada.core.enums.OrdemDirecao;

public class OrdenacaoUtil {

    public static SortField<?> getOrdenacao(String propriedade, OrdemDirecao direcao) {
        if (Objects.nonNull(propriedade) && Objects.nonNull(direcao)) {
            if (OrdemDirecao.ASC.equals(direcao)) {
                return DSL.field(propriedade).asc();
            }

            return DSL.field(propriedade).desc();
        }

        return null;
    }

}
