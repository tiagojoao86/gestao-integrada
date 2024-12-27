package br.com.grupopipa.gestaointegrada.core.dao;

import java.util.Objects;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import br.com.grupopipa.gestaointegrada.core.dto.Filtro;
import br.com.grupopipa.gestaointegrada.core.dto.FiltroItem;
import br.com.grupopipa.gestaointegrada.core.enums.FiltroOperador;
import br.com.grupopipa.gestaointegrada.core.enums.FiltroOperadorLogico;

public class FiltroUtil {

    public static Condition getConditionFromFiltro(Filtro filtro) {
        Condition condition = null;
        for (FiltroItem item : filtro.getItems()) {
            if (Objects.isNull(condition)) {
                condition = getCondition(item.getCampo(), item.getValores(), item.getOperador());
                continue;
            }

            if (FiltroOperadorLogico.E.equals(filtro.getOperadorLogico())) {
                condition = condition.and(getCondition(item.getCampo(), item.getValores(), item.getOperador()));
                continue;
            }

            if (FiltroOperadorLogico.OU.equals(filtro.getOperadorLogico())) {
                condition = condition.or(getCondition(item.getCampo(), item.getValores(), item.getOperador()));
            }

        }

        return condition;
    }

    private static Condition getCondition(String campo, Object[] valor, FiltroOperador operador) {
        if (Objects.nonNull(valor) && valor.length > 0) {
            switch (operador) {
                case IGUAL:
                    return DSL.field(campo).eq(valor[0]);
                case DIFERENTE:
                    return DSL.field(campo).ne(valor[0]);
                case MAIOR:
                    return DSL.field(campo).gt(valor[0]);
                case MENOR:
                    return DSL.field(campo).lt(valor[0]);
                case MAIOR_IGUAL:
                    return DSL.field(campo).greaterOrEqual(valor[0]);
                case MENOR_IGUAL:
                    return DSL.field(campo).lessOrEqual(valor[0]);
                case CONTEM:
                    return DSL.field(campo).contains(valor[0]);
                case IN:
                    return DSL.field(campo).in(valor);
                default:
                    throw new RuntimeException(String.format("%s %s %s",
                            "Erro ao montar os filtros da consulta, não foi informado um operador válido.",
                            "Campo: " + campo,
                            "Opções válidas: " + String.join(", ",
                                    Stream.of(FiltroOperador.values()).map(FiltroOperador::name).toList())));
            }
        }

        throw new RuntimeException(String.format("%s %s",
                "Erro ao montar os filtros da consulta, não foi informado um valor.",
                "Campo: " + campo));
    }

}
