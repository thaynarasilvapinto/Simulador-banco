package com.github.thaynarasilvapinto.SimuladorBanco.domain;


import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

@Entity
public class Operacao {
    private Integer idOrigem;
    private Integer idDestino;
    private Integer idOpercao;
    private double valorOperacao;
    private String dataOperacao;
    private TipoOperacao tipoOpercao;

    public Operacao() {
    }

    public Operacao(Integer idOrigem, Integer idDestino, double valorTransacao, TipoOperacao tipoOperacao) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.valorOperacao = valorTransacao;
        this.tipoOpercao = tipoOperacao;

        Locale locale = new Locale("pt","BR");
        GregorianCalendar calendario = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH':'mm",locale);
        dataOperacao = formatador.format(calendario.getTime());
    }

    public Integer getIdOrigem() {
        return idOrigem;
    }

    public Integer getIdDestino() {
        return idDestino;
    }

    public Integer getIdOpercao() {
        return idOpercao;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public void setIdOpercao(Integer idOpercao) {
        this.idOpercao = idOpercao;
    }
}
