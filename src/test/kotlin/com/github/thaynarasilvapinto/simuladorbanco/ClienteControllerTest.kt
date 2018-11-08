package com.github.thaynarasilvapinto.simuladorbanco

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.thaynarasilvapinto.simuladorbanco.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ClienteResponse
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
import kotlin.test.assertEquals

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
    private lateinit var joao: Cliente
    private lateinit var gson: Gson
    val mapper: ObjectMapper by lazy {
        ObjectMapper().registerModule(JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(KotlinModule())
    }

    @Before
    fun setUp() {
        createClient()
        this.gson = Gson()
    }

    private fun createClient() {
        joao = clienteService.criarCliente(Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "055.059.396-94",
                conta = Conta(saldo = 0.00)))
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        contaService.delete(joao.conta.id)
    }

    @Test
    fun `deve retonar o cliente solicitado`() {
        this.mvc.perform(get("/cliente/{id}", joao.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo { mvcResult ->
                    val response = mapper.readValue(mvcResult.response.contentAsString, ClienteResponse::class.java)
                    assertEquals(joao.cpf, response.cpf)
                    assertEquals(joao.nome, response.nome)
                    assertEquals(joao.conta.id, response.conta)
                }
    }

    @Test
    fun `Nao deve retornar um cliente quando o id eh invalido`() {
        this.mvc.perform(get("/cliente/{id}", -1))
                .andExpect(status().isUnprocessableEntity)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun `Deve criar um cliente`() {
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
    fun `Nao deve criar um cliente que ja existe no banco`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "055.059.396-94")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun `Nao deve criar um cliente com CPF invalido`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "151.425.426-")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }
}