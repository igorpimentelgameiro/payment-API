package com.igorpimentelgameiro.payment_system.validation;

public class ValidaPagamento {
    private final boolean condicao;
    private final String mensagemErro;

    public ValidaPagamento(boolean condicao, String mensagemErro) {
        this.condicao = condicao;
        this.mensagemErro = mensagemErro;
    }

    public boolean isInvalida() {
        return condicao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }
}
