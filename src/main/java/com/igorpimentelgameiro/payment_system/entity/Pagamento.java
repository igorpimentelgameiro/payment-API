package com.igorpimentelgameiro.payment_system.entity;

import com.igorpimentelgameiro.payment_system.dto.PagamentoDTO;
import com.igorpimentelgameiro.payment_system.enums.MetodoPagamento;
import com.igorpimentelgameiro.payment_system.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigoPagamento;

    private Integer documento;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private Integer numeroCartao;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    private boolean isAtivo;

    public Pagamento(PagamentoDTO pagamentoDTO) {
        this.documento = pagamentoDTO.documento();
        this.metodoPagamento = pagamentoDTO.metodoPagamento();
        this.numeroCartao = pagamentoDTO.numeroCartao();
        this.valor = pagamentoDTO.valor();
        this.statusPagamento = StatusPagamento.PENDENTE_PROCESSAMENTO;
        this.isAtivo = Boolean.TRUE;
    }

    public void atualizarStatus(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}