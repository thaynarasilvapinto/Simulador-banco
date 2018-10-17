package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "operacao")
public class Operacao implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idOperacao;
    private double valorOperacao;
    private String dataOperacao;
    private TipoOperacao tipoOperacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="conta_id_origem")
    private Conta contaOrigem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="conta_id_destino")
    private Conta contaDestino;

    public Operacao() {
    }
    public Operacao(Conta contaDestino, Conta contaOrigem, double valorTransacao, TipoOperacao tipoOperacao) {
        this.contaDestino = contaDestino;
        this.contaOrigem = contaOrigem;
        this.valorOperacao = valorTransacao;
        this.tipoOperacao = tipoOperacao;

        Locale locale = new Locale("pt","BR");
        GregorianCalendar calendario = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH':'mm",locale);
        dataOperacao = formatador.format(calendario.getTime());
    }


    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public Integer getIdOperacao() {
        return idOperacao;
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

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public void setIdOperacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operacao)) return false;
        Operacao operacao = (Operacao) o;
        return Objects.equals(getIdOperacao(), operacao.getIdOperacao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdOperacao());
    }
}
