package com.igorpimentelgameiro.payment_system.dto;

import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;

public record DetalhamentoPagamentoDTO(
        Long codigoPagamento,
        String documento,
        MetodoPagamento metodoPagamento,
        Double Valor,
        Integer numeroCartao,
        StatusPagamento statusPagamento,
        Boolean isAtivo
) {
    public DetalhamentoPagamentoDTO(Pagamento pagamento) {
        this(pagamento.getCodigoPagamento(), String.valueOf(pagamento.getDocumento()), pagamento.getMetodoPagamento(), pagamento.getValorPagamento(), pagamento.getNumeroCartao(), pagamento.getStatusPagamento(), pagamento.isAtivo());
    }

}
