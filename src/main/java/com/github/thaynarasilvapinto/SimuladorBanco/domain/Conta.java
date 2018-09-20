package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private double saldo;
    private String dataHora;
    private ArrayList<Operacao> extrato = new ArrayList<Operacao>();

    public Conta() {
    }
    public Conta(String dataHora){
        this.id = id;
        saldo = 0.00;
        this.dataHora = dataHora;
    }
    public int saque(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            this.extrato.add(operacao);
            return 1;
        }
        return 0;
    }
    public int deposito(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo +=operacao.getValorOperacao();
            this.extrato.add(operacao);
            return 1;
        }
        return 0;
    }
    public int Transferencia(Conta clienteDestino, Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo){
            efetuarTrasferencia(operacao);
            clienteDestino.recebimentoTransferencia(operacao);
            return 1;
        }
        return 0;
    }
    public void recebimentoTransferencia(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo += operacao.getValorOperacao();
            this.extrato.add(operacao);
        }
    }
    public void efetuarTrasferencia(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            this.extrato.add(operacao);
        }
    }
    public double getSaldo() {
        return saldo;
    }

    public Integer getId() {
        return id;
    }


    public ArrayList<Operacao> getExtrato() {
        return extrato;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta)) return false;
        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}