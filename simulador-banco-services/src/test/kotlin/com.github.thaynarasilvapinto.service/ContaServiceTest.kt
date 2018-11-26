package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.model.repository.ContaRepository
import com.github.thaynarasilvapinto.model.repository.OperacaoRepository
import com.github.thaynarasilvapinto.service.config.ServiceBaseTest
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.BalanceIsInsufficientException
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ContaServiceTest : ServiceBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()

    private val repositoryConta: ContaRepository = mock()
    private val repositoryOperacao: OperacaoRepository = mock()

    private lateinit var contaService: ContaService
    private lateinit var operacaoService: OperacaoService


    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var maria: Cliente
    private lateinit var contaMaria: Conta

    @Before
    fun setup() {
        contaService = ContaService(repositoryConta, repositoryOperacao)
        operacaoService = OperacaoService(repositoryOperacao, repositoryConta)
        createClient()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joao = Cliente(
            nome = "Conta Test Joao Service",
            cpf = "151.425.426-75",
            conta = joaoConta
        )

        contaMaria = Conta(saldo = 0.00)
        maria = Cliente(
            nome = "Conta Test Maria Service",
            cpf = "086.385.420-62",
            conta = contaMaria
        )

    }

    @Test
    fun `deve buscar a conta`() {
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)
        val contaBuscada = contaService.find(joaoConta.id)
        assertEquals(joaoConta.id, contaBuscada!!.id)
        verify(repositoryConta, times(1)).findById(joaoConta.id)
    }

    @Test
    fun update() {
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)
        joaoConta.saldo = 100.00
        whenever(repositoryConta.update(joaoConta)).thenReturn(1)
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)

        contaService.update(joaoConta)
        val conta = contaService.find(joaoConta.id)

        assertEquals(joaoConta.saldo, conta!!.saldo, 0.00001)
    }

    @Test
    fun `deve buscar o saldo`() {
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)
        val conta = contaService.saldo(joaoConta.id)
        val saldo = conta.saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)
    }

    @Test
    fun `ao solicitar um saldo de uma conta invalida deve retornar execao`() {
        whenever(repositoryConta.findById("-1")).thenReturn(null)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.saldo(joaoConta.id)
    }

    @Test
    fun `Ao solicitar um extrato, devera constar toda movimentacao da conta`() {

        val deposito = Operacao(
            valorOperacao = 200.00,
            contaDestino = joaoConta,
            contaOrigem = joaoConta,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO
        )
        val saque = Operacao(
            valorOperacao = 100.00,
            contaDestino = joaoConta,
            contaOrigem = joaoConta,
            tipoOperacao = Operacao.TipoOperacao.SAQUE
        )
        val deposito2 = Operacao(
            valorOperacao = 400.00,
            contaDestino = joaoConta,
            contaOrigem = joaoConta,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO
        )

        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)

        whenever(
            repositoryOperacao
                .findAllByContaDestinoAndTipoOperacao(
                    joaoConta.id,
                    Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA.name
                )
        )
            .thenReturn(listOfNotNull())
        whenever(
            repositoryOperacao
                .findAllByContaOrigemAndTipoOperacao(joaoConta.id, Operacao.TipoOperacao.TRANSFERENCIA.name)
        )
            .thenReturn(listOfNotNull())
        whenever(
            repositoryOperacao
                .findAllByContaOrigemAndTipoOperacao(joaoConta.id, Operacao.TipoOperacao.DEPOSITO.name)
        )
            .thenReturn(listOf(deposito, deposito2))
        whenever(
            repositoryOperacao
                .findAllByContaOrigemAndTipoOperacao(joaoConta.id, Operacao.TipoOperacao.SAQUE.name)
        )
            .thenReturn(listOf(saque))
        val extratoJoao = contaService.extrato(joaoConta.id)

        verify(repositoryConta, times(1)).findById(joaoConta.id)
        verify(repositoryOperacao, times(1)).findAllByContaDestinoAndTipoOperacao(
            joaoConta.id,
            Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA.name
        )
        verify(repositoryOperacao, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoConta.id,
            Operacao.TipoOperacao.TRANSFERENCIA.name
        )
        verify(repositoryOperacao, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoConta.id,
            Operacao.TipoOperacao.DEPOSITO.name
        )
        verify(repositoryOperacao, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoConta.id,
            Operacao.TipoOperacao.SAQUE.name
        )
        verify(repositoryOperacao, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoConta.id,
            Operacao.TipoOperacao.DEPOSITO.name
        )

        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[0].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[1].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.SAQUE, extratoJoao[2].tipoOperacao)
    }

    @Test
    fun `Ao solicitar um extrato de uma conta invalida, devera retornar uma excecao`() {
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(null)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.extrato("-1")
    }

    @Test
    fun `O saldo da conta nunca podera ser negativo`() {
        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)

        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.saque(
            id = joaoConta.id,
            valor = 100.00
        )

    }

    @Test
    fun `deve devolver saldo`() {

        joaoConta.saldo = 200.00

        whenever(repositoryConta.findById(joaoConta.id)).thenReturn(joaoConta)

        val saldo = contaService.saldo(joaoConta.id)
        assertEquals(200.00, saldo.saldo, 0.00001)
    }
}