package com.github.thaynarasilvapinto.SimuladorBanco.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;


@Entity
public class Cliente implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cpf;
    private String dataHora;
    @OneToOne
    private Conta conta;

    public Cliente() {
    }

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;

        Locale locale = new Locale("pt","BR");
        GregorianCalendar calendario = new GregorianCalendar();
        SimpleDateFormat formatador = new SimpleDateFormat("dd'/'MM'/'yyyy' - 'HH':'mm",locale);
        dataHora = formatador.format(calendario.getTime());
        this.conta = new Conta(dataHora);

    }

    public Integer getId() {
        return id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
