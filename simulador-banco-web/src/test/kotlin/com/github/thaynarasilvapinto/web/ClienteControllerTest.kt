package com.github.thaynarasilvapinto.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.thaynarasilvapinto.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.api.response.ClienteResponse
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.service.ClienteService
import com.github.thaynarasilvapinto.service.ContaService
import com.github.thaynarasilvapinto.web.config.ControllerBaseTest
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertEquals

class ClienteControllerTest : ControllerBaseTest() {
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
    fun setup() {
        createClient()
        this.gson = Gson()
    }

    private fun createClient() {
        joao = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test Cliente Controller",
                cpf = "055.059.396-94",
                conta = Account(saldo = 0.00)
            )
        )!!
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

        this.mvc.perform(
            post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        val cliente = clienteService.findCPF("182.562.790-87")
        clienteService.delete(cliente!!.id)
        contaService.delete(cliente.conta.id)
    }

    @Test
    fun `Nao deve criar um cliente que ja existe no banco`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "055.059.396-94")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(
            post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun `Nao deve criar um cliente com CPF invalido`() {
        val clienteCriarRequest = ClienteCriarRequest(nome = "Cliente Test Maria", cpf = "151.425.426-")
        val content = gson.toJson(clienteCriarRequest)

        this.mvc.perform(
            post("/criar-cliente")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }
}