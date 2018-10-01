package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private double saldo;
    private String dataHora;
    @OneToMany(mappedBy = "contaOrigem",cascade = CascadeType.ALL)
    private List<Operacao> extrato;
    private int contID = 0; //TODO:Essa variavel foi feita para implementar um id do cliente, porém ela sera retirada
    @OneToOne(mappedBy = "conta")
    private Cliente cliente;

    protected Conta() {
    }
    public Conta(double saldo){
        this.saldo = saldo;
        Locale locale = new Locale("pt","BR");
        GregorianCalendar calendario = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH':'mm",locale);
        dataHora = formatador.format(calendario.getTime());
        this.extrato = new ArrayList<Operacao>();
    }
    public Operacao saque(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            this.extrato.add(operacao);
            return operacao;
        }
        return null;
    }
    public Operacao deposito(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo +=operacao.getValorOperacao();
            this.extrato.add(operacao);
            return operacao;
        }
        return null;
    }
    public Operacao Transferencia(Conta clienteDestino, Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo){
            Operacao op = efetuarTrasferencia(operacao);
            clienteDestino.recebimentoTransferencia(operacao);
            return op;
        }
        return null;
    }
    public Operacao recebimentoTransferencia(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo += operacao.getValorOperacao();
           // Operacao op = new Operacao(operacao.getIdOrigem(),operacao.getIdDestino(),operacao.getValorOperacao(),TipoOperacao.RECEBIMENTO_TRANSFERENCIA);
            this.extrato.add(operacao);
            return  operacao;
        }
        return null;
    }
    public Operacao efetuarTrasferencia(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
           /// Operacao op = new Operacao(operacao.getIdOrigem(),operacao.getIdDestino(),operacao.getValorOperacao(),TipoOperacao.TRANSFERENCIA);
            this.extrato.add(operacao);
            return operacao;
        }
        return null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public int getContID() {
        return contID;
    }

    public void setContID(int contID) {
        this.contID = contID;
    }

    public List<Operacao> getExtrato() {
        return extrato;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setExtrato(List<Operacao> extrato) {
        if (extrato == null) {
            extrato = new ArrayList<>();
        }
        this.extrato = extrato;
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