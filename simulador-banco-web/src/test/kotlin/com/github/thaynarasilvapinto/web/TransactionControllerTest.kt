package com.github.thaynarasilvapinto.web

import com.github.thaynarasilvapinto.api.request.OperacaoRequest
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.service.ClienteService
import com.github.thaynarasilvapinto.service.ContaService
import com.github.thaynarasilvapinto.service.OperacaoService
import com.github.thaynarasilvapinto.web.config.ControllerBaseTest
import com.google.gson.Gson
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class TransactionControllerTest : ControllerBaseTest() {
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var gson: Gson
    private lateinit var joao: Cliente
    private lateinit var maria: Cliente

    private lateinit var joaoAccount: Account
    private lateinit var mariaAccount: Account

    private lateinit var transactionDepositoJoao: Transaction
    private lateinit var transactionSaqueJoao: Transaction
    private lateinit var transactionTransferencia: Transaction

    @Before
    fun setup() {
        createClient()
        this.gson = Gson()

        this.transactionDepositoJoao = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = joaoAccount,
            valorOperacao = 200.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO
        )
        this.transactionSaqueJoao = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = joaoAccount,
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.SAQUE
        )
        this.transactionTransferencia = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = mariaAccount,
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.TRANSFERENCIA
        )
    }

    private fun createClient() {
        joao = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "055.059.396-94",
                conta = Account(saldo = 0.00)
            )
        )!!
        maria = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "177.082.896-67",
                conta = Account(saldo = 0.00)
            )
        )!!
        joaoAccount = joao.conta
        mariaAccount = maria.conta
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        clienteService.delete(maria.id)
        var extrato = operacaoService.findAllContaOrigem(joaoAccount)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        extrato = operacaoService.findAllContaOrigem(mariaAccount)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)

        }
        contaService.delete(joaoAccount.id)
        contaService.delete(mariaAccount.id)
    }

    @Test
    fun `deve fazer deposito`() {
        val operacaoDepositoRequest = OperacaoRequest(valorOperacao = 500.00, contaDestino = null)
        val content = gson.toJson(operacaoDepositoRequest)
        this.mvc.perform(
            post("/conta/{id}/deposito", joaoAccount.id)
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

        joaoAccount.saldo = 300.00
        contaService.update(joaoAccount)

        val operacaoSaqueRequest = OperacaoRequest(valorOperacao = 200.00, contaDestino = null)
        val content = gson.toJson(operacaoSaqueRequest)
        this.mvc.perform(
            post("/conta/{id}/saque", joaoAccount.id)
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
        joaoAccount.saldo = 300.00
        contaService.update(joaoAccount)

        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 100.00, contaDestino = mariaAccount.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoAccount.id)
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
            post("/conta/{id}/deposito", joaoAccount.id)
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
            post("/conta/{id}/saque", joaoAccount.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Nao deve realizar transferencia negativa`() {
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = -100.00, contaDestino = mariaAccount.id)
        val content = gson.toJson(operacaoTransferenciaRequest)

        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoAccount.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
    }

/*    @Test
    private fun `Nao deve realizar transferencia para a mesma conta`() {
        joaoAccount.saldo = 300.00
        contaService.update(joaoAccount)
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, accountDestino = joaoAccount.id)
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoAccount.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }*/

    @Test
    fun `Nao deve transferir para uma conta invalida`() {
        joaoAccount.saldo = 300.00
        contaService.update(joaoAccount)
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = "-1")
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", joaoAccount.id)
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Nao deve transferir de uma conta que nao existe`() {
        val operacaoTransferenciaRequest = OperacaoRequest(valorOperacao = 300.00, contaDestino = mariaAccount.id)
        val content = gson.toJson(operacaoTransferenciaRequest)
        this.mvc.perform(
            post("/conta/{id}/transferencia", "-1")
                .content(content)
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isUnprocessableEntity)
    }

}