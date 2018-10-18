////package com.github.thaynarasilvapinto.simuladorbanco;
//
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta;
//import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService;
//import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService;
//import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService;
//import com.google.gson.Gson;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ContaControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private ClienteService clienteService;
//    @Autowired
//    private ContaService contaService;
//    @Autowired
//    private OperacaoService operacaoService;
//    private Cliente joao;
//    private Conta joaoConta;
//    private Gson gson;
//
//
//    @Before
//    public void setup() {
//        createClient();
//        this.gson = new Gson();
//    }
//
//    private Cliente createClient() {
//        joaoConta = new Conta(0.00);
//        this.joao = new Cliente("Conta Test ClienteController", "15142542675", null);
//        clienteService.insert(joao);
//        contaService.insert(joaoConta);
//        joao.setConta(joaoConta);
//        clienteService.update(joao);
//        return new Cliente();
//    }
//
//    @After
//    public void delete() {
//        clienteService.delete(joao.getId());
//        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
//        for (int i = 0; i < extrato.size(); i++) {
//            operacaoService.delete(extrato.get(i).getIdOperacao());
//        }
//        contaService.delete(joaoConta.getId());
//    }
//
//    @Test
//    public void deveRetornarAContasBuscada() throws Exception {
//        String body = gson.toJson(joaoConta);
//        this.mvc.perform(get("/conta/{id}", joaoConta.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().json(body));
//
//    }
//
//    @Test
//    public void deveRetornarOSaldoDaConta() throws Exception {
//        joaoConta.setSaldo(100);
//        contaService.update(joaoConta);
//
//        this.mvc.perform(get("/conta/{id}/saldo", joaoConta.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Saldo: " + joaoConta.getSaldo()));
//    }
//}
