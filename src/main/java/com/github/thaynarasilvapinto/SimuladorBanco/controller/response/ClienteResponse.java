package com.github.thaynarasilvapinto.SimuladorBanco.controller.response;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;

public class ClienteResponse {
    private Integer id;
    private String nome;
    private String cpf;
    private String dataHora;
    private ContaResponse conta;

    public ClienteResponse() {
    }

    public ClienteResponse(Integer id, String nome, String cpf, String dataHora, ContaResponse conta) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataHora = dataHora;
        this.conta = conta;
    }
    public ClienteResponse(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.dataHora = cliente.getDataHora();
        this.conta = new ContaResponse(cliente.getConta());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public ContaResponse getConta() {
        return conta;
    }

    public void setConta(ContaResponse conta) {
        this.conta = conta;
    }
}
