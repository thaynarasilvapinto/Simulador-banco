package com.github.thaynarasilvapinto.SimuladorBanco.controller.request;

public class OperacaoDepositoRequest {
    private Double valorOperacao;

    public OperacaoDepositoRequest() {
    }

    public OperacaoDepositoRequest(Double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public Double getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(Double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }
}
