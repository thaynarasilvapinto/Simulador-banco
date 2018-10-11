package com.github.thaynarasilvapinto.SimuladorBanco;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoDepositoRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoSaqueRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoTransferenciaRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.OperacaoResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperacaoControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContaService contaService;
    @Autowired
    private OperacaoService operacaoService;
    private Gson gson;
    private ObjectMapper mapper;
    private Cliente joao;
    private Cliente maria;

    private Conta joaoConta;
    private Conta mariaConta;

    private Operacao operacaoDepositoJoao;
    private Operacao operacaoSaqueJoao;
    private Operacao operacaoTransferencia;

    @Before
    public void setup() {
        createClient();
        this.gson = new Gson();
        this.mapper = new ObjectMapper();

        this.operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 200.00, TipoOperacao.DEPOSITO);
        this.operacaoSaqueJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.SAQUE);
        this.operacaoTransferencia = new Operacao(joaoConta, mariaConta, 100, TipoOperacao.TRANSFERENCIA);
    }

    private void createClient() {
        joaoConta = new Conta(0.00);
        joao = new Cliente("Joao Operacao Test ClienteController", "15142542675", null);
        mariaConta = new Conta(0.00);
        maria = new Cliente("Maria Operacao Test ClienteController", "17708289667", null);

        joao = clienteService.insert(joao);
        joaoConta = contaService.insert(joaoConta);
        joao.setConta(joaoConta);
        clienteService.update(joao);

        maria = clienteService.insert(maria);
        mariaConta = contaService.insert(mariaConta);
        maria.setConta(mariaConta);
        clienteService.update(maria);
    }

    @After
    public void delete() {
        clienteService.delete(joao.getId());
        clienteService.delete(maria.getId());
        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
        for (int i = 0; i < extrato.size(); i++) {
            operacaoService.delete(extrato.get(i).getIdOperacao());
        }
        extrato = operacaoService.findAllContaOrigem(mariaConta);
        for (int i = 0; i < extrato.size(); i++) {
            operacaoService.delete(extrato.get(i).getIdOperacao());
        }
        contaService.delete(joaoConta.getId());
        contaService.delete(mariaConta.getId());
    }


    @Test
    public void deveRetornarAOperacaosBuscada() throws Exception {

        operacaoDepositoJoao = operacaoService.insert(operacaoDepositoJoao);
        joaoConta.deposito(operacaoDepositoJoao);
        contaService.update(joaoConta);

        String body = gson.toJson(operacaoDepositoJoao);

        this.mvc.perform(get("/operacao/{id}", operacaoDepositoJoao.getIdOperacao()))
                .andExpect(status().isOk())
                .andExpect(content().json(body));

    }

    @Test
    public void deveRealizarDeposito() throws Exception {
        OperacaoDepositoRequest operacaoDepositoRequest = new OperacaoDepositoRequest(500.00);
        String content = gson.toJson(operacaoDepositoRequest);
        this.mvc.perform(post("/conta/{id}/deposito", joaoConta.getId())
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
                .andDo(mvcResult -> {
                    OperacaoResponse response
                            = mapper.readValue(mvcResult.getResponse().getContentAsString(), OperacaoResponse.class);
                    assertEquals(500, response.getContaOrigem().getSaldo(), 0.0001);
                });


    }

    @Test
    public void deveRealizarSaque() throws Exception {

        joaoConta.setSaldo(300);
        contaService.update(joaoConta);

        OperacaoSaqueRequest operacaoSaqueRequest = new OperacaoSaqueRequest(200.00);
        String content = gson.toJson(operacaoSaqueRequest);
        this.mvc.perform(post("/conta/{id}/saque", joaoConta.getId())
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
                .andDo(mvcResult -> {
                    OperacaoResponse response
                            = mapper.readValue(mvcResult.getResponse().getContentAsString(), OperacaoResponse.class);
                    assertEquals(100, response.getContaOrigem().getSaldo(), 0.0001);
                });
    }

    @Test
    public void deveRealizarTransferencia() throws Exception {
        joaoConta.setSaldo(300);
        contaService.update(joaoConta);

        OperacaoTransferenciaRequest operacaoTransferenciaRequest =
                new OperacaoTransferenciaRequest(100, mariaConta.getId());
        String content = gson.toJson(operacaoTransferenciaRequest);

        this.mvc.perform(post("/conta/{id}/transferencia", joaoConta.getId())
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
                .andDo(mvcResult -> {
                    OperacaoResponse response
                            = mapper.readValue(mvcResult.getResponse().getContentAsString(), OperacaoResponse.class);
                    assertEquals(200, response.getContaOrigem().getSaldo(), 0.0001);
                    assertEquals(100, response.getContaDestino().getSaldo(), 0.0001);
                });


    }

}
