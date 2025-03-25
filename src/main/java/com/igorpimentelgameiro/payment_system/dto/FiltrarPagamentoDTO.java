package com.igorpimentelgameiro.payment_system.dto;

import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;

public record FiltrarPagamentoDTO(
        String codigoPagamento,
        String documento,
        MetodoPagamento metodoPagamento,
        String numeroCartao,
        Double valor,
        StatusPagamento statusPagamento
) {
}
