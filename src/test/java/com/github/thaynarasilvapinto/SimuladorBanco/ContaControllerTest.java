package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.OperacaoResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import com.google.gson.Gson;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContaControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContaService contaService;
    @Autowired
    private OperacaoService operacaoService;
    private Cliente joao;
    private Conta joaoConta;
    private Gson gson;


    @Before
    public void setup() {
        createClient();
        this.gson = new Gson();
    }

    private Cliente createClient() {
        joaoConta = new Conta(0.00);
        this.joao = new Cliente("Conta Test ClienteController", "15142542675", null);
        clienteService.insert(joao);
        contaService.insert(joaoConta);
        joao.setConta(joaoConta);
        clienteService.update(joao);
        return new Cliente();
    }
    @After
    public void delete() {
        clienteService.delete(joao.getId());
        List<Operacao> extrato = operacaoService.findAllContaOrigem(joaoConta);
        for (int i = 0; i < extrato.size(); i++) {
            operacaoService.delete(extrato.get(i).getIdOperacao());
        }
        contaService.delete(joaoConta.getId());
    }

    @Test
    public void deveRetornarAContasBuscado() throws Exception {
        Gson gson = new Gson();
        String body = gson.toJson(joaoConta);
        this.mvc.perform(get("/conta/{id}", joaoConta.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(body));

    }

    @Test
    public void deveRetornarOSaldoDaConta() throws Exception {
         joaoConta.setSaldo(100);
         contaService.update(joaoConta);

        this.mvc.perform(get("/conta/{id}", joaoConta.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Saldo: " + joaoConta.getSaldo()));
    }
    @Test
    public void deveRetornarOExtratoDeUmCliente() throws Exception {
        Operacao operacaoDepositoJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.DEPOSITO);
        Operacao operacaoSaqueJoao = new Operacao(joaoConta, joaoConta, 100.00, TipoOperacao.SAQUE);

        joao.getConta().deposito(operacaoDepositoJoao);
        joao.getConta().saque(operacaoSaqueJoao);

        operacaoService.insert(operacaoDepositoJoao);
        operacaoService.insert(operacaoSaqueJoao);

        List<Operacao> listaOperacao = operacaoService.findAllContaOrigem(joaoConta);
        List<OperacaoResponse> extrato = new ArrayList<>();
        for (int i = 0; i < listaOperacao.size(); i++) {
            extrato.add(new OperacaoResponse(listaOperacao.get(i)));
        }


        //String body = gson.toJson(extrato);

        //System.out.println("################## \n\n\n"+ body + "\n\n\n#################");
/*
        this.mvc.perform(get("/conta/{id}/extrato", joaoConta.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
       */
    }
}
