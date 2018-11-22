package com.github.thaynarasilvapinto.web.services

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.web.ControllerBaseTest
import com.github.thaynarasilvapinto.web.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.web.services.exception.BalanceIsInsufficientException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.beans.factory.annotation.Autowired

class ContaServiceTest : ControllerBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var maria: Cliente
    private lateinit var contaMaria: Conta

    @Before
    fun setup() {
        createClient()
    }

    private fun createClient() {
        joao = clienteService.criarCliente(
            Cliente(
                nome = "Conta Test Joao Service",
                cpf = "151.425.426-75",
                conta = Conta(saldo = 0.00)
            )
        )
        joaoConta = joao.conta
        maria = clienteService.criarCliente(
            Cliente(
                nome = "Conta Test Maria Service",
                cpf = "086.385.420-62",
                conta = Conta(saldo = 0.00)
            )
        )
        contaMaria = maria.conta
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }

        contaService.delete(joaoConta.id)

        clienteService.delete(maria.id)
        val extratoMaria = operacaoService.findAllContaOrigem(contaMaria)
        for (i in extratoMaria.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }

        contaService.delete(contaMaria.id)
    }

    @Test
    fun `deve buscar a conta`() {
        val conta = contaService.find(joaoConta.id)
        val id = conta.get().id
        assertEquals(joaoConta.id, id)
    }

    @Test
    fun update() {
        joaoConta.saldo = 100.00
        contaService.update(joaoConta)
        val conta = contaService.find(joaoConta.id)
        val saldo = conta.get().saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)
    }

    @Test
    fun `deve buscar o saldo`() {
        val conta = contaService.find(joaoConta.id)
        val saldo = conta.get().saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)

    }

    @Test
    fun `Ao solicitar um extrato, devera constar toda movimentacao da conta`() {

        operacaoService.deposito(
            id = joaoConta.id,
            valor = 200.00
        )
        operacaoService.saque(
            id = joaoConta.id,
            valor = 100.00
        )
        operacaoService.deposito(
            id = joaoConta.id,
            valor = 400.00
        )

        val extratoJoao = contaService.extrato(joaoConta.id)
        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[0].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.SAQUE, extratoJoao[1].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[2].tipoOperacao)
    }

    @Test
    fun `Ao solicitar um extrato de uma conta invalida, devera retornar uma excecao`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.extrato("-1")
    }

    @Test
    fun `O saldo da conta nunca podera ser negativo`() {
        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")
        operacaoService.deposito(
            id = joaoConta.id,
            valor = 10.00
        )
        operacaoService.saque(
            id = joaoConta.id,
            valor = 100.00
        )

    }

    @Test
    fun `deve devolver saldo`() {
        operacaoService.deposito(
            id = joaoConta.id,
            valor = 200.00
        )
        operacaoService.saque(
            id = joaoConta.id,
            valor = 100.00
        )
        operacaoService.deposito(
            id = joaoConta.id,
            valor = 400.00
        )

        val saldo = contaService.saldo(joaoConta.id)

        assertEquals(500.00, saldo.saldo, 0.00001)
    }
}