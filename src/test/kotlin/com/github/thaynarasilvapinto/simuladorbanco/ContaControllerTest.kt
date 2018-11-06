package com.github.thaynarasilvapinto.simuladorbanco


import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseSaldo
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
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
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        createClient()
        this.gson = Gson()
    }

    private fun createClient() {
        joao = clienteService.criarCliente(Cliente(
                nome = "Cliente Test ClienteController",
                cpf = "055.059.396-94",
                conta = -1))
        joaoConta = contaService.find(joao.conta).get()
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(joaoConta.id)
    }

    @Test
    fun `Deve retornar a conta`() {
        this.mvc.perform(get("/conta/{id}", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

    }

    @Test
    fun `Nao deve retornar uma conta que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}", -1))
                .andExpect(status().isUnprocessableEntity)

    }

    @Test
    fun `Deve retornar o saldo`() {
        val body = gson.toJson(joaoConta.toResponseSaldo())
        this.mvc.perform(get("/conta/{id}/saldo", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().string(body))
    }

    @Test
    fun `Nao deve retornar o saldo de uma conta que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}/saldo", -1))
                .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `Deve retornar o extrato de um cliente`() {

        var operacaoDeposito = Operacao(
                contaOrigem = joaoConta.id,
                contaDestino = joaoConta.id,
                valorOperacao = 200.00,
                tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
        var operacaoSaque = Operacao(
                contaOrigem = joaoConta.id,
                contaDestino = joaoConta.id,
                valorOperacao = 100.00,
                tipoOperacao = Operacao.TipoOperacao.SAQUE)

        operacaoService.insert(operacaoDeposito)
        operacaoService.insert(operacaoSaque)

        this.mvc.perform(get("/conta/{id}/extrato", joaoConta.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
    }

    @Test
    fun `Nao deve retornar o extrato de um cliente que nao existe no banco`() {
        this.mvc.perform(get("/conta/{id}/extrato", -1))
                .andExpect(status().isUnprocessableEntity)
    }
}