package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

@Entity
public class Operacao {
    private Integer idOrigem;
    private Integer idDestino;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
