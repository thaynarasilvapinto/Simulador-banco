package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.controller.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ClienteResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
class ClienteControllerTest(
        private var mvc: MockMvc,
        private var clienteService: ClienteService,
        private var contaService: ContaService,
        private var joaoConta: Conta,
        private var joao: Cliente,
        private var gson: Gson
) {

    @Before
    fun setup() {
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
    fun delete() {
        clienteService.delete(joao.id)
        contaService.delete(joaoConta.id)
    }

    @Test
    @Throws(Exception::class)
    fun `deve retornar o cliente buscado`() {
        val body = gson.toJson(ClienteResponse(joao))
        this.mvc.perform(get("/cliente/{id}", joao!!.id))
                .andExpect(status().isOk)
                .andExpect(content().json(body))

    }

    @Test
    @Throws(Exception::class)
    fun `deve criar um cliente`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "382.927.350-97")
        val content = gson.toJson(clienteCriarRequest)

        val cliente = clienteService.findCPF("382.927.350-97")
        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        clienteService.delete(cliente.get().id)
        contaService.delete(cliente.get().conta.id)
    }
}



