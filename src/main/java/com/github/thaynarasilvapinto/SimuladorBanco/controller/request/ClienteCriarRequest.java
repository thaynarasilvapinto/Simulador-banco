package com.github.thaynarasilvapinto.SimuladorBanco.controller.request;

import javax.validation.constraints.NotNull;

public class ClienteCriarRequest {
    @NotNull
    private String nome;
    @NotNull
    private String cpf;

    public ClienteCriarRequest() {
    }

    public ClienteCriarRequest(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
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
}
