package com.igorpimentelgameiro.payment_system.util;

import com.igorpimentelgameiro.payment_system.dto.AtualizarPagamentoDTO;
import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagamentoMock {

    public static PagamentoDTO getPagDTOMock_Sucesso() {
        return new PagamentoDTO(
                (double) 123L,
                "123456789",
                MetodoPagamento.BOLETO,
                null
        );
    }


    public static PagamentoDTO getPagDTOMock_Falha_Pix() {
        return new PagamentoDTO(
                (double) 123L,
                "123456789",
                MetodoPagamento.PIX,
                123456789
        );
    }


    public static PagamentoDTO getPagDTOMock_Falha_Boleto() {
        return new PagamentoDTO(
                (double) 123L,
                "123456789",
                MetodoPagamento.BOLETO,
                123456789
        );
    }


    public static PagamentoDTO getPagDTOMock_Falha_Cartao_Credito() {
        return new PagamentoDTO(
                (double) 123L,
                "123456789",
                MetodoPagamento.CARTAO_CREDITO,
                null
        );
    }


    public static PagamentoDTO getPagDTOMock_Falha_Cartao_Debito() {
        return new PagamentoDTO(
                (double) 123L,
                "123456789",
                MetodoPagamento.CARTAO_DEBITO,
                null
        );
    }


    public static AtualizarPagamentoDTO getAtualizarPagDTOMock_Sucesso() {
        return new AtualizarPagamentoDTO(
                1,
                StatusPagamento.PROCESSADO_SUCESSO
        );
    }


    public static AtualizarPagamentoDTO getAtualizarPagDTOMock_Falha_Caso1() {
        return new AtualizarPagamentoDTO(
                1,
                StatusPagamento.PROCESSADO_FALHA
        );
    }

}
