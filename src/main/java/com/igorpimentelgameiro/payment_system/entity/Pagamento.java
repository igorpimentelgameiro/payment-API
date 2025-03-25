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
@Table(name = "PAGAMENTO")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigoPagamento;

    @Column(name = "DOCUMENTO", nullable = false)
    private String documento;

    @Column(name = "VALOR", nullable = false)
    private Double valorPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_PAGAMENTO", nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(name = "NUMERO_CARTAO", nullable = true)
    private Integer numeroCartao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_PAGAMENTO", nullable = false)
    private StatusPagamento statusPagamento;

    @Column(name = "STATUS", nullable = false)
    private boolean isAtivo;

    public Pagamento(PagamentoDTO pagamentoDTO) {
        this.documento = pagamentoDTO.getDocumento();
        this.metodoPagamento = pagamentoDTO.getMetodoPagamento();
        this.numeroCartao = pagamentoDTO.getNumeroCartao();
        this.valorPagamento = pagamentoDTO.getValorPagamento();
        this.statusPagamento = StatusPagamento.PENDENTE_PROCESSAMENTO;
        this.isAtivo = Boolean.TRUE;
    }

    public void atualizarStatus(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}