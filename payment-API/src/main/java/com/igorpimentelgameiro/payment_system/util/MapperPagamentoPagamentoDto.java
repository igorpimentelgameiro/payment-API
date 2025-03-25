package com.igorpimentelgameiro.payment_system.util;

import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.entity.Pagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;

public class MapperPagamentoPagamentoDto {

    public static PagamentoDTO toDTO(Pagamento pagamento) {
        if (pagamento == null) {
            return null;
        }
        return new PagamentoDTO(
                pagamento.getValorPagamento(),
                pagamento.getDocumento(),
                pagamento.getMetodoPagamento(),
                pagamento.getNumeroCartao()
        );
    }

    public static Pagamento toEntity(PagamentoDTO pagamentoDTO) {
        if (pagamentoDTO == null) {
            return null;
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setDocumento(pagamentoDTO.getDocumento());
        pagamento.setValorPagamento(pagamentoDTO.getValorPagamento());
        pagamento.setMetodoPagamento(pagamentoDTO.getMetodoPagamento());
        pagamento.setNumeroCartao(pagamentoDTO.getNumeroCartao());
        
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE_PROCESSAMENTO);
        pagamento.setAtivo(true); // Marcando como ativo

        return pagamento;
    }
}
