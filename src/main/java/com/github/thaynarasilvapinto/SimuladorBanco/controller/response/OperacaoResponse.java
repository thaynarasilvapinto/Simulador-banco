package com.github.thaynarasilvapinto.SimuladorBanco.controller.response;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;

public class OperacaoResponse {
    private Integer idOperacao;
    private double valorOperacao;
    private String dataOperacao;
    private String tipoOperacao;
    private ContaResponse contaOrigem;
    private ContaResponse contaDestino;

    public OperacaoResponse(Integer idOperacao, double valorOperacao, String dataOperacao, String tipoOperacao, ContaResponse contaOrigem, ContaResponse contaDestino) {
        this.idOperacao = idOperacao;
        this.valorOperacao = valorOperacao;
        this.dataOperacao = dataOperacao;
        this.tipoOperacao = tipoOperacao;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    public OperacaoResponse(Operacao operacao) {
        this.idOperacao = operacao.getIdOperacao();
        this.valorOperacao = operacao.getValorOperacao();
        this.dataOperacao = operacao.getDataOperacao();
        this.tipoOperacao = operacao.getTipoOperacao().name();
        this.contaOrigem = new ContaResponse(operacao.getContaOrigem());
        this.contaDestino = new ContaResponse(operacao.getContaDestino());
    }

    public Integer getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(double valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public String getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(String dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public ContaResponse getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(ContaResponse contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public ContaResponse getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(ContaResponse contaDestino) {
        this.contaDestino = contaDestino;
    }
}
