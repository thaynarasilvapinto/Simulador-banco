package com.github.thaynarasilvapinto.SimuladorBanco.controller.request;

import java.io.DataOutput;

public class OperacaoSaqueRequest {
    private Double valorOperacao;

    public OperacaoSaqueRequest() {
    }

    public OperacaoSaqueRequest(Double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public Double getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(Double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }
}
