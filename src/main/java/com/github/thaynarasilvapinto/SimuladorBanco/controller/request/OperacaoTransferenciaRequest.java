package com.github.thaynarasilvapinto.SimuladorBanco.controller.request;

import javax.validation.constraints.Min;

public class OperacaoTransferenciaRequest {
    @Min(1)
    private double valorOperacao;
    private Integer contaDestino;

    public OperacaoTransferenciaRequest() {
    }

    public OperacaoTransferenciaRequest(double valorOperacao, Integer contaDestino) {
        this.valorOperacao = valorOperacao;
        this.contaDestino = contaDestino;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public Integer getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Integer contaDestino) {
        this.contaDestino = contaDestino;
    }
}
