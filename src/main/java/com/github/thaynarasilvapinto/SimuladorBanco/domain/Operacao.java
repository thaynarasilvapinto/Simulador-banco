package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Operacao {
    private Integer idOrigem;
    private Integer idDestino;
    private Integer idOpercao;
    private double valorOperacao;
    private String dataOperacao;
    private TipoOperacao tipoOperacao;

    public Operacao() {
    }

    public Operacao(Integer idOrigem, Integer idDestino, double valorTransacao, TipoOperacao tipoOperacao) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
        this.valorOperacao = valorTransacao;
        this.tipoOperacao = tipoOperacao;

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

    public Integer getIdOpercao(int i) {
        return idOpercao;
    }

    public double getValorOperacao() {
        return valorOperacao;
    }

    public void setIdOrigem(Integer idOrigem) {
        this.idOrigem = idOrigem;
    }

    public void setIdDestino(Integer idDestino) {
        this.idDestino = idDestino;
    }

    public Integer getIdOpercao() {
        return idOpercao;
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

    public void setIdOpercao(Integer idOpercao) {
        this.idOpercao = idOpercao;
    }
}
