package com.github.thaynarasilvapinto.SimuladorBanco.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Min(0)
    private double saldo;
    private String dataHora;

    protected Conta() {
    }
    public Conta(double saldo){
        this.saldo = saldo;
        Locale locale = new Locale("pt","BR");
        GregorianCalendar calendario = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH':'mm",locale);
        dataHora = formatador.format(calendario.getTime());
    }
    public Operacao saque(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo && operacao.getValorOperacao() > 0) {
            saldo -= operacao.getValorOperacao();
            return operacao;
        }
        return null;
    }

    public Operacao deposito(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo +=operacao.getValorOperacao();
            return operacao;
        }
        return null;
    }
    public Operacao Transferencia(Conta clienteDestino, Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo && operacao.getValorOperacao() > 0){
            Operacao op = efetuarTrasferencia(operacao);
            clienteDestino.recebimentoTransferencia(operacao);
            return op;
        }
        return null;
    }
    public Operacao recebimentoTransferencia(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo += operacao.getValorOperacao();
            return  operacao;
        }
        return null;
    }
    public Operacao efetuarTrasferencia(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            return operacao;
        }
        return null;
    }

    public double getSaldo() {
        return saldo;
    }

    public Integer getId() {
        return id;
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
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta)) return false;
        Conta conta = (Conta) o;
        return Objects.equals(getId(), conta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}