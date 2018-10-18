////package com.github.thaynarasilvapinto.simuladorbanco;
//
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class ClienteServiceTest {
//
//    @Autowired
//    private ClienteService clienteService;
//    @Autowired
//    private ContaService contaService;
//    private Cliente joao;
//    private Conta joaoConta;
//
//    @Before
//    public void setup() {
//        createClient();
//    }
//    private Cliente createClient() {
//        joaoConta = new Conta(0.00);
//        joao = new Cliente("Cliente Test Cliente", "15142542675", null);
//        joao = clienteService.insert(joao);
//        joaoConta = contaService.insert(joaoConta);
//        joao.setConta(joaoConta);
//        joao = clienteService.update(joao);
//        return new Cliente();
//    }
//    @After
//    public void delete() {
//        clienteService.delete(joao.getId());
//        contaService.delete(joaoConta.getId());
//    }
//    @Test
//    public void buscar() {
//        Cliente clienteBuscado = clienteService.find(joao.getId());
//        assertEquals(joao.getId(),clienteBuscado.getId());
//    }
//    @Test
//    public void update() {
//
//        joao.setNome("Client Test Update");
//        joao = clienteService.update(joao);
//        Cliente clienteBuscado = clienteService.find(joao.getId());
//        assertEquals(joao.getNome(),clienteBuscado.getNome());
//    }
//}
