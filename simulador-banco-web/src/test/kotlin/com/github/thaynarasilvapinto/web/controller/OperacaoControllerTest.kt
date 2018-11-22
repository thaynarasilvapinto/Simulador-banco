package com.github.thaynarasilvapinto.web.controller

import com.github.thaynarasilvapinto.api.request.OperacaoRequest
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.web.ControllerBaseTest
import com.github.thaynarasilvapinto.web.services.ClienteService
import com.github.thaynarasilvapinto.web.services.ContaService
import com.github.thaynarasilvapinto.web.services.OperacaoService
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class OperacaoControllerTest : ControllerBaseTest() {
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var gson: Gson
    private lateinit var joao: Cliente
    private lateinit var maria: Cliente

    private lateinit var joaoConta: Conta
    private lateinit var mariaConta: Conta

    private lateinit var operacaoDepositoJoao: Operacao
    private lateinit var operacaoSaqueJoao: Operacao
    private lateinit var operacaoTransferencia: Operacao

    @Before
    fun setup() {
        createClient()
        this.gson = Gson()

        this.operacaoDepositoJoao = Operacao(
            contaOrigem = joaoConta,
            contaDestino = joaoConta,
            valorOperacao = 200.00,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO
        )
        this.operacaoSaqueJoao = Operacao(
            contaOrigem = joaoConta,
            contaDestino = joaoConta,
            valorOperacao = 100.00,
            tipoOperacao = Operacao.TipoOperacao.SAQUE
        )
        this.operacaoTransferencia = Operacao(
            contaOrigem = joaoConta,
            contaDestino = mariaConta,
            valorOperacao = 100.00,
            tipoOperacao = Operacao.TipoOperacao.TRANSFERENCIA
        )
    }

    private fun createClient() {
        joao = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "055.059.396-94",
                conta = Conta(saldo = 0.00)
            )
        )
        maria = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "177.082.896-67",
                conta = Conta(saldo = 0.00)
            )
        )
        joaoConta = joao.conta
        mariaConta = maria.conta
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        clienteService.delete(maria.id)
        var extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        extrato = operacaoService.findAllContaOrigem(mariaConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)

        }
        contaService.delete(joaoConta.id)
        contaService.delete(mariaConta.id)
    }

    @Test
    fun `deve fazer deposito`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = 500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(
            post("/conta/{id}/deposito", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    fun `Nao deve depositar em uma conta que nao existe`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = 500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(
            post("/conta/{id}/deposito", "-1")
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Deve realizar saque`() {

        joaoConta.saldo = 300.00
        contaService.update(joaoConta)

        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = 200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(
            post("/conta/{id}/saque", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    fun `Nao deve realizar saque de conta que nao existe`() {
        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = 200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(
            post("/conta/{id}/saque", "-1")
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `deve realizar transferencia`() {
        joaoConta.saldo = 300.00
        contaService.update(joaoConta)

        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 100.00, contaDestino = mariaConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    fun `Nao deve realizar deposito abaixo de 1`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = -500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(
            post("/conta/{id}/deposito", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Nao deve realizar uma saque negativo`() {
        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = -200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(
            post("/conta/{id}/saque", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Nao deve realizar transferencia negativa`() {
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = -100.00, contaDestino = mariaConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    private fun `Nao deve realizar transferencia para a mesma conta`() {
        joaoConta.saldo = 300.00
        contaService.update(joaoConta)
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = joaoConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Nao deve transferir para uma conta invalida`() {
        joaoConta.saldo = 300.00
        contaService.update(joaoConta)
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = "-1")
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Nao deve transferir de uma conta que nao existe`() {
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = mariaConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", "-1")
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

}