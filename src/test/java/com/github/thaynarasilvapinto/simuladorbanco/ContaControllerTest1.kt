package com.github.thaynarasilvapinto.simuladorbanco

//package com.github.thaynarasilvapinto.simuladorbanco;

import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ContaResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest(
        @Autowired
        private val mvc: MockMvc,
        @Autowired
        private val clienteService: ClienteService,
        @Autowired
        private val contaService: ContaService,
        @Autowired
        private val operacaoService: OperacaoService,
        private var joao: Cliente,
        private var joaoConta: Conta,
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
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(joaoConta.id)
    }

    @Test
    @Throws(Exception::class)
    fun `deve retonar a conta buscada`() {
        val body = gson.toJson(ContaResponse(joaoConta))
        this.mvc.perform(get("/conta/{id}", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().json(body))

    }

    @Test
    @Throws(Exception::class)
    fun `deve retonar o saldo buscado`() {
        val body = gson.toJson(ContaResponse(joaoConta))
        this.mvc.perform(get("/conta/{id}/saldo", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().string(body))
    }
}
