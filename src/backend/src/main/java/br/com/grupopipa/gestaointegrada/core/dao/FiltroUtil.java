package br.com.grupopipa.gestaointegrada.core.dao;

import java.util.ArrayList;
import java.util.List;
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
                condition = getCondition(item);
                continue;
            }

            if (FiltroOperadorLogico.E.equals(filtro.getOperadorLogico())) {
                condition = condition.and(getCondition(item));
                continue;
            }

            if (FiltroOperadorLogico.OU.equals(filtro.getOperadorLogico())) {
                condition = condition.or(getCondition(item));
            }

        }

        return condition;
    }

    private static Condition getCondition(FiltroItem filtroItem) {
        FiltroOperador operador = filtroItem.getOperador();
        String campo = filtroItem.getCampo();
        List<Object> valor = new ArrayList<>();

        if (Objects.nonNull(filtroItem.getDatas())) {
            valor.addAll(Stream.of(filtroItem.getDatas()).toList());
            campo = "DATE_TRUNC('day', " + campo + ")";
            return buildCondition(valor, campo, operador);
        }

        if (Objects.nonNull(filtroItem.getDatasHora())) {
            valor.addAll(Stream.of(filtroItem.getDatasHora()).toList());
            return buildCondition(valor, campo, operador);
        }

        if (Objects.nonNull(filtroItem.getNumeros())) {
            valor.addAll(Stream.of(filtroItem.getNumeros()).toList());
            return buildCondition(valor, campo, operador);
        }

        valor.addAll(Stream.of(filtroItem.getTextos()).toList());
        return buildCondition(valor, campo, operador);
    }

    private static Condition buildCondition(List<Object> valor, String campo, FiltroOperador operador) {
        if (Objects.nonNull(valor) && valor.size() > 0) {
            switch (operador) {
                case IGUAL:
                    if (valor.get(0) instanceof String)
                        return DSL.field(campo).equalIgnoreCase(valor.get(0).toString());

                    return DSL.field(campo).eq(valor.get(0));
                case DIFERENTE:
                    if (valor.get(0) instanceof String)
                        return DSL.field(campo).notEqualIgnoreCase(valor.get(0).toString());
                    return DSL.field(campo).ne(valor.get(0));
                case MAIOR:
                    return DSL.field(campo).gt(valor.get(0));
                case MENOR:
                    return DSL.field(campo).lt(valor.get(0));
                case MAIOR_IGUAL:
                    return DSL.field(campo).greaterOrEqual(valor.get(0));
                case MENOR_IGUAL:
                    return DSL.field(campo).lessOrEqual(valor.get(0));
                case CONTEM:
                    return DSL.field(campo).containsIgnoreCase(valor.get(0));
                case NAO_CONTEM:
                    return DSL.field(campo).notContainsIgnoreCase(valor.get(0));
                case IN:
                    return DSL.field(campo).in(valor);
                case NOT_IN:
                    return DSL.field(campo).notIn(valor);
                case ENTRE:
                    return DSL.field(campo).between(valor.get(0), valor.get(1));
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
