package com.igorpimentelgameiro.payment_system.dto;

import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoDTO(
        @NotNull
        Integer documento,
        @NotNull
        Double valor,
        @NotNull
        MetodoPagamento metodoPagamento,
        Integer numeroCartao
) {
    public PagamentoDTO(Pagamento pagamentoAnterior) {
        this(pagamentoAnterior.getDocumento(), pagamentoAnterior.getValor(), pagamentoAnterior.getMetodoPagamento(), pagamentoAnterior.getNumeroCartao());
    }
}
