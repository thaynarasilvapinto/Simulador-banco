package com.github.thaynarasilvapinto.simuladorbanco

//package com.github.thaynarasilvapinto.simuladorbanco;

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.thaynarasilvapinto.simuladorbanco.controller.request.OperacaoRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.DepositoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.SaqueResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.TransferenciaResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class OperacaoControllerTest{
    @Autowired
    private lateinit var mvc: MockMvc
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

        this.operacaoDepositoJoao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 200.00, tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
        this.operacaoSaqueJoao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 100.00, tipoOperacao = Operacao.TipoOperacao.SAQUE)
        this.operacaoTransferencia = Operacao(contaOrigem = joaoConta, contaDestino = mariaConta, valorOperacao = 100.00, tipoOperacao = Operacao.TipoOperacao.TRANSFERENCIA)
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        joao = Cliente(nome = "Joao Operacao Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        mariaConta = Conta(saldo = 0.00)
        mariaConta = contaService.insert(mariaConta)
        maria = Cliente(nome = "Maria Operacao Test ClienteController", cpf = "177.082.896-67", conta = mariaConta)
        joao = clienteService.insert(joao)
        maria = clienteService.insert(maria)
    }

    @After
    fun delete() {
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
    @Throws(Exception::class)
    fun `deve realizar deposito`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = 500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(post("/conta/{id}/deposito", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun ` deve realizar saque`() {

        joaoConta.saldo = 300.00
        contaService.update(joaoConta)

        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = 200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(post("/conta/{id}/saque", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun `deve realizar transferencia`() {
        joaoConta.saldo = 300.00
        contaService.update(joaoConta)

        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 100.00, contaDestino = mariaConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.idOperacao", notNullValue()))
    }

    @Test
    @Throws(Exception::class)
    fun `nao deve realizar deposito`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = -500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(post("/conta/{id}/deposito", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `nao deve realizar saque`() {
        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = -200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(post("/conta/{id}/saque", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `nao deve trealizar transfefrencia de valor negativo`() {
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = -100.00, contaDestino = mariaConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `nao deve trealizar transfefrencia para mesmo conta`() {
        joaoConta.saldo = 300.00
        contaService.update(joaoConta)
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = joaoConta.id)
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(post("/conta/{id}/transferencia", joaoConta.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity)
    }

}
