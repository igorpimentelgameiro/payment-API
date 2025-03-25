package com.igorpimentelgameiro.payment_system.util;

import com.igorpimentelgameiro.payment_system.dto.DetalhamentoPagamentoDTO;
import com.igorpimentelgameiro.payment_system.entity.Pagamento;

public class MapperDetalhamentoPagamento {

    public static Pagamento toEntity(DetalhamentoPagamentoDTO detalhamentoPagamentoDTO) {
        if (detalhamentoPagamentoDTO == null) {
            return null;
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setCodigoPagamento(detalhamentoPagamentoDTO.codigoPagamento());
        pagamento.setDocumento(detalhamentoPagamentoDTO.documento());
        pagamento.setMetodoPagamento(detalhamentoPagamentoDTO.metodoPagamento());
        pagamento.setValorPagamento(detalhamentoPagamentoDTO.Valor());
        pagamento.setNumeroCartao(detalhamentoPagamentoDTO.numeroCartao());
        pagamento.setStatusPagamento(detalhamentoPagamentoDTO.statusPagamento());
        pagamento.setAtivo(detalhamentoPagamentoDTO.isAtivo());
        return pagamento;
    }
    
    public static DetalhamentoPagamentoDTO toDTO(Pagamento pagamento) {
        if (pagamento == null) {
            return null;
        }
        return new DetalhamentoPagamentoDTO(
                pagamento.getCodigoPagamento(),
                pagamento.getDocumento(),
                pagamento.getMetodoPagamento(),
                pagamento.getValorPagamento(),
                pagamento.getNumeroCartao(),
                pagamento.getStatusPagamento(),
                pagamento.isAtivo()
        );
    }
}
