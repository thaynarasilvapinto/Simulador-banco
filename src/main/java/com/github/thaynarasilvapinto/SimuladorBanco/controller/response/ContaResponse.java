package com.github.thaynarasilvapinto.SimuladorBanco.controller.response;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;

import java.util.List;
import java.util.stream.Collectors;

public class ContaResponse {
    private Integer id;
    private double saldo;
    private String dataHora;
    private List<OperacaoResponse> extrato;
    private ClienteResponse cliente;

    public ContaResponse() {
    }

    public ContaResponse(Integer id, double saldo, String dataHora, List<OperacaoResponse> extrato, ClienteResponse cliente) {
        this.id = id;
        this.saldo = saldo;
        this.dataHora = dataHora;
    }

    public ContaResponse(Conta conta) {
        this.id = conta.getId();
        this.saldo = conta.getSaldo();
        this.dataHora = conta.getDataHora();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }
}
