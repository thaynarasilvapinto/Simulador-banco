package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.controller.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    private lateinit var joaoConta: Conta
    private lateinit var joao: Cliente
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        createClient()
        this.gson = Gson()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        contaService.delete(joaoConta.id)
    }

    @Test
    @Throws(Exception::class)
    fun `should return the requested client`() {
        this.mvc.perform(get("/cliente/{id}", joao.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType((MediaType.APPLICATION_JSON_UTF8)))

    }
    @Test
    @Throws(Exception::class)
    fun `should not return a client that does not exist in banc`() {
        this.mvc.perform(get("/cliente/{id}", -1))
                .andExpect(status().isUnprocessableEntity)
    }
    @Test
    @Throws(Exception::class)
    fun `must create a client`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "182.562.790-87")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        val cliente = clienteService.findCPF("182.562.790-87")
        clienteService.delete(cliente.get().id)
        contaService.delete(cliente.get().conta.id)
    }
    @Test
    @Throws(Exception::class)
    fun `should not create a client that does exist in the bank`(){
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "151.425.426-75")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }
}



