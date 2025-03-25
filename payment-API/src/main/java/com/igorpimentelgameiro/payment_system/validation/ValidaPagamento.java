package com.igorpimentelgameiro.payment_system.validation;

import lombok.Getter;

public class ValidaPagamento {
    private final boolean condicao;
    @Getter
    private final String mensagemErro;

    public ValidaPagamento(boolean condicao, String mensagemErro) {
        this.condicao = condicao;
        this.mensagemErro = mensagemErro;
    }

    public boolean isInvalida() {
        return condicao;
    }

}
