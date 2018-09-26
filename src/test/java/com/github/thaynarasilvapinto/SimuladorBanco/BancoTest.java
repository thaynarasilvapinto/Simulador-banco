package com.github.thaynarasilvapinto.SimuladorBanco;


import com.github.thaynarasilvapinto.SimuladorBanco.domain.Banco;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BancoTest {

    private Cliente joao;
    private Cliente maria;
    private Banco banco;

    @Before
    public void criaUmAmbiente() {
        this.banco = new Banco();
        this.joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        this.maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        banco.add(joao);
        banco.add(maria);
    }
    @Test
    public void deveTerDoisClientesNoBanco(){
        assertEquals(2,banco.getClientes().size());
        assertEquals(joao,banco.getClientes().get(0));
        assertEquals(maria,banco.getClientes().get(1));

    }
    @Test
    public void deveDevolverCliente(){
        assertEquals(2,banco.getClientes().size());
        assertEquals(joao,banco.find(0));
        assertEquals(maria,banco.find(1));
    }
    @Test
    public void deveDevolverConta(){
        assertEquals(2,banco.getClientes().size());
        assertEquals(joao.getConta(),banco.findConta(0));
        assertEquals(maria.getConta(),banco.findConta(1));
    }
    @Test
    public void naoDeveAddClientesComCPFigual(){
        Cliente obj = new Cliente("Thales Joao", "177.082.896-67");
        banco.add(obj);
        assertEquals(2,banco.getClientes().size());
    }
    @Test
    public void deveAddClientesComCPFsDiferentes(){
        assertEquals(2,banco.getClientes().size());
    }
}
