package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private double saldo;
    private String dataHora;
    @OneToMany(mappedBy = "contaOrigem",cascade = CascadeType.ALL)
    private List<Operacao> extrato = new ArrayList<Operacao>();
    private int contID = 0; //TODO:Essa variavel foi feita para implementar um id do cliente, porém ela sera retirada
    @OneToOne(mappedBy = "conta")
    private Cliente cliente;

    protected Conta() {
    }
    public Conta(String dataHora){
        saldo = 0.00;
        this.dataHora = dataHora;
    }
    public Operacao saque(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            operacao.setIdOperacao(contID);
            contID++;
            this.extrato.add(operacao);
            return operacao;
        }
        return null;
    }
    public Operacao deposito(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo +=operacao.getValorOperacao();
            operacao.setIdOperacao(contID);
            contID++;
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
    public void recebimentoTransferencia(Operacao operacao){
        if(operacao.getValorOperacao() > 0){
            saldo += operacao.getValorOperacao();
            Operacao op = new Operacao(operacao.getIdOrigem(),operacao.getIdDestino(),operacao.getValorOperacao(),TipoOperacao.RECEBIMENTO_TRANSFERENCIA);
            op.setIdOperacao(contID);
            contID++;
            this.extrato.add(op);
        }
    }
    public Operacao efetuarTrasferencia(Operacao operacao){
        if(operacao.getValorOperacao() <= this.saldo) {
            saldo -= operacao.getValorOperacao();
            Operacao op = new Operacao(operacao.getIdOrigem(),operacao.getIdDestino(),operacao.getValorOperacao(),TipoOperacao.TRANSFERENCIA);
            op.setIdOperacao(contID);
            contID++;
            this.extrato.add(op);
            return op;
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