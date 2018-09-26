package com.github.thaynarasilvapinto.SimuladorBanco;


import com.github.thaynarasilvapinto.SimuladorBanco.domain.Banco;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BancoTest {
    @Test
    public void naoDeveTerNadaNoBanco(){
        Banco banco = new Banco();
        assertEquals(0,banco.getClientes().size());
    }
    @Test
    public void deveTerDoisClientesNoBanco(){
        Banco banco = new Banco();
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        banco.add(joao);
        banco.add(maria);

        assertEquals(2,banco.getClientes().size());
        assertEquals(joao,banco.getClientes().get(0));
        assertEquals(maria,banco.getClientes().get(1));

    }
    @Test
    public void deveDevolverCliente(){
        Banco banco = new Banco();
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        banco.add(joao);
        banco.add(maria);

        assertEquals(2,banco.getClientes().size());
        assertEquals(joao,banco.find(0));
        assertEquals(maria,banco.find(1));
    }
    @Test
    public void deveDevolverConta(){
        Banco banco = new Banco();
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "177.082.896-67");
        banco.add(joao);
        banco.add(maria);

        assertEquals(2,banco.getClientes().size());
        assertEquals(joao.getConta(),banco.findConta(0));
        assertEquals(maria.getConta(),banco.findConta(1));
    }
    @Test
    public void naoDeveAddClientesComCPFigual(){
        Banco banco = new Banco();
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "151.425.426-75");
        banco.add(joao);
        banco.add(maria);
        assertEquals(1,banco.getClientes().size());
    }
    @Test
    public void deveAddClientesComCPFsDiferentes(){
        Banco banco = new Banco();
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        Cliente maria = new Cliente("Maria Abadia de Oliveira", "462.821.146-91");
        banco.add(joao);
        banco.add(maria);
        assertEquals(2,banco.getClientes().size());
    }
}
