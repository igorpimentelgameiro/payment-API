package com.igorpimentelgameiro.payment_system.dto;

import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagamentoDTO {

    @NotNull
    private String documento;

    @NotNull
    @Positive(message = "O valor do pagamento deve ser maior que zero.")
    private Double valorPagamento;

    @NotNull
    private MetodoPagamento metodoPagamento;

    private Integer numeroCartao;

    public PagamentoDTO(Double valorPagamento, String documento, MetodoPagamento metodoPagamento, Integer numeroCartao) {
        this.valorPagamento = valorPagamento;
        this.documento = documento;
        this.metodoPagamento = metodoPagamento;
        this.numeroCartao = numeroCartao;
    }

    public Double getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Integer getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(Integer numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
}
