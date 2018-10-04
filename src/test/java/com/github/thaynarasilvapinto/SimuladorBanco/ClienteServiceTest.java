package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.repositories.ClienteRepository;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;


    @Before
    public void criaUmAmbiente() {
        clienteService = new ClienteService();
    }

    @Mock
    private ClienteRepository repo;

    @Test
    public void naoDeveBuscarClienteDoBanco(){

        when(repo.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        Cliente cliente = clienteService.find(10);

        assertNull(cliente);

    }
}
