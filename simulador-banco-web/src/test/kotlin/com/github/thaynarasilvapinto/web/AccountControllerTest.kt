package com.github.thaynarasilvapinto.web


import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.service.ClienteService
import com.github.thaynarasilvapinto.service.ContaService
import com.github.thaynarasilvapinto.service.OperacaoService
import com.github.thaynarasilvapinto.web.config.ControllerBaseTest
import com.github.thaynarasilvapinto.web.utils.toResponseSaldo
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class AccountControllerTest : ControllerBaseTest() {
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoAccount: Account
    private lateinit var gson: Gson

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
        joaoAccount = joao.conta
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        val extrato = operacaoService.findAllContaOrigem(joaoAccount)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(joaoAccount.id)
    }

    @Test
    fun `Deve retornar a conta`() {
        this.mvc.perform(get("/conta/{id}", joaoAccount.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

    }

    @Test
    fun `Nao deve retornar uma conta que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}", "-1"))
            .andExpect(status().isUnprocessableEntity)

    }

    @Test
    fun `Deve retornar o saldo`() {
        val body = gson.toJson(joaoAccount.toResponseSaldo())
        this.mvc.perform(get("/conta/{id}/saldo", joaoAccount.id))
            .andExpect(status().isOk)
            .andExpect(content().string(body))
    }

    @Test
    fun `Nao deve retornar o saldo de uma conta que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}/saldo", "-1"))
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Deve retornar o extrato de um cliente`() {

        val operacaoDeposito = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = joaoAccount,
            valorOperacao = 200.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO
        )
        val operacaoSaque = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = joaoAccount,
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.SAQUE
        )

        operacaoService.insert(operacaoDeposito)
        operacaoService.insert(operacaoSaque)

        this.mvc.perform(get("/conta/{id}/extrato", joaoAccount.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun `Nao deve retornar o extrato de um cliente que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}/extrato", "-1"))
            .andExpect(status().isUnprocessableEntity)
    }
}