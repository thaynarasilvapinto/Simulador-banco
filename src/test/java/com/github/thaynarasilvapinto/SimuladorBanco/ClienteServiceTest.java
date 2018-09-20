package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ClienteServiceTest {
    @Autowired
    private ClienteService service;

    @Test
    public void deveInserirCliente(){
        Cliente joao = new Cliente("João Pedro da Silva", "151.425.426-75");
        service.insert(joao);
        Cliente obj = service.find(1);
        assertEquals(joao,obj);
    }
}
