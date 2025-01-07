package br.com.grupopipa.gestaointegrada.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.grupopipa.gestaointegrada.core.enums.FiltroOperador;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FiltroItem {
    private String campo;
    private FiltroOperador operador;
    private String[] textos;
    private LocalDate[] datas;
    private LocalDateTime[] datasHora;
    private BigDecimal[] numeros;
}