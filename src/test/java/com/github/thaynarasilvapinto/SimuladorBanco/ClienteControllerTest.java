package com.github.thaynarasilvapinto.SimuladorBanco;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ContaService contaService;
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
        this.joao = new Cliente("Cliente Test ClienteController", "15142542675", null);
        clienteService.insert(joao);
        contaService.insert(joaoConta);
        joao.setConta(joaoConta);
        clienteService.update(joao);
        return new Cliente();
    }
    @After
    public void delete() {
        clienteService.delete(joao.getId());
        contaService.delete(joaoConta.getId());
    }

    @Test
    public void deveRetornarOClienteBuscado() throws Exception {
        String body = gson.toJson(joao);
        this.mvc.perform(get("/cliente/{id}", joao.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(body));

    }
    @Test
    public void deveCriarUmCliente() throws Exception {
        String content =
                "{\n" +
                        "    \"nome\": \"João da Silva Test\",\n" +
                        "    \"cpf\": \"38292735097\"\n" +
                        "}";
        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        Cliente cliente = clienteService.findCPF("38292735097");

        clienteService.delete(cliente.getId());
        contaService.delete(cliente.getConta().getId());
    }
}



