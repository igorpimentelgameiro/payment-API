package com.igorpimentelgameiro.payment_system.dto;

import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarPagamentoDTO(
        @NotNull
        Integer codigoPagamento,
        @NotNull
        StatusPagamento statusPagamento
) {
}
