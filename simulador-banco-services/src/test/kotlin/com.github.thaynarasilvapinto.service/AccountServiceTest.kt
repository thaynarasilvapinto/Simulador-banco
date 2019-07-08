package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
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

class AccountServiceTest : ServiceBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()

    private val repositoryAccount: AccountRepository = mock()
    private val repositoryTransaction: TransactionRepository = mock()

    private lateinit var contaService: ContaService
    private lateinit var operacaoService: OperacaoService


    private lateinit var joao: Cliente
    private lateinit var joaoAccount: Account
    private lateinit var maria: Cliente
    private lateinit var accountMaria: Account

    @Before
    fun setup() {
        contaService = ContaService(repositoryAccount, repositoryTransaction)
        operacaoService = OperacaoService(repositoryTransaction, repositoryAccount)
        createClient()
    }

    private fun createClient() {
        joaoAccount = Account(saldo = 0.00)
        joao = Cliente(
            nome = "Account Test Joao Service",
            cpf = "151.425.426-75",
            conta = joaoAccount
        )

        accountMaria = Account(saldo = 0.00)
        maria = Cliente(
            nome = "Account Test Maria Service",
            cpf = "086.385.420-62",
            conta = accountMaria
        )

    }

    @Test
    fun `deve buscar a conta`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        val contaBuscada = contaService.find(joaoAccount.id)
        assertEquals(joaoAccount.id, contaBuscada!!.id)
        verify(repositoryAccount, times(1)).findById(joaoAccount.id)
    }

    @Test
    fun update() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        joaoAccount.saldo = 100.00
        whenever(repositoryAccount.update(joaoAccount)).thenReturn(1)
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        contaService.update(joaoAccount)
        val conta = contaService.find(joaoAccount.id)

        assertEquals(joaoAccount.saldo, conta!!.saldo, 0.00001)
    }

    @Test
    fun `deve buscar o saldo`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        val conta = contaService.saldo(joaoAccount.id)
        val saldo = conta.saldo
        assertEquals(joaoAccount.saldo, saldo, 0.00001)
    }

    @Test
    fun `ao solicitar um saldo de uma conta invalida deve retornar execao`() {
        whenever(repositoryAccount.findById("-1")).thenReturn(null)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.saldo(joaoAccount.id)
    }

    @Test
    fun `Ao solicitar um extrato, devera constar toda movimentacao da conta`() {

        val deposito = Transaction(
            valorOperacao = 200.00,
            accountDestino = joaoAccount,
            accountOrigem = joaoAccount,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO
        )
        val saque = Transaction(
            valorOperacao = 100.00,
            accountDestino = joaoAccount,
            accountOrigem = joaoAccount,
            tipoOperacao = Transaction.TipoOperacao.SAQUE
        )
        val deposito2 = Transaction(
            valorOperacao = 400.00,
            accountDestino = joaoAccount,
            accountOrigem = joaoAccount,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO
        )

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        whenever(
            repositoryTransaction
                .findAllByContaDestinoAndTipoOperacao(
                    joaoAccount.id,
                    Transaction.TipoOperacao.RECEBIMENTO_TRANSFERENCIA.name
                )
        )
            .thenReturn(listOfNotNull())
        whenever(
            repositoryTransaction
                .findAllByContaOrigemAndTipoOperacao(joaoAccount.id, Transaction.TipoOperacao.TRANSFERENCIA.name)
        )
            .thenReturn(listOfNotNull())
        whenever(
            repositoryTransaction
                .findAllByContaOrigemAndTipoOperacao(joaoAccount.id, Transaction.TipoOperacao.DEPOSITO.name)
        )
            .thenReturn(listOf(deposito, deposito2))
        whenever(
            repositoryTransaction
                .findAllByContaOrigemAndTipoOperacao(joaoAccount.id, Transaction.TipoOperacao.SAQUE.name)
        )
            .thenReturn(listOf(saque))
        val extratoJoao = contaService.extrato(joaoAccount.id)

        verify(repositoryAccount, times(1)).findById(joaoAccount.id)
        verify(repositoryTransaction, times(1)).findAllByContaDestinoAndTipoOperacao(
            joaoAccount.id,
            Transaction.TipoOperacao.RECEBIMENTO_TRANSFERENCIA.name
        )
        verify(repositoryTransaction, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoAccount.id,
            Transaction.TipoOperacao.TRANSFERENCIA.name
        )
        verify(repositoryTransaction, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoAccount.id,
            Transaction.TipoOperacao.DEPOSITO.name
        )
        verify(repositoryTransaction, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoAccount.id,
            Transaction.TipoOperacao.SAQUE.name
        )
        verify(repositoryTransaction, times(1)).findAllByContaOrigemAndTipoOperacao(
            joaoAccount.id,
            Transaction.TipoOperacao.DEPOSITO.name
        )

        assertEquals(Transaction.TipoOperacao.DEPOSITO, extratoJoao[0].tipoOperacao)
        assertEquals(Transaction.TipoOperacao.DEPOSITO, extratoJoao[1].tipoOperacao)
        assertEquals(Transaction.TipoOperacao.SAQUE, extratoJoao[2].tipoOperacao)
    }

    @Test
    fun `Ao solicitar um extrato de uma conta invalida, devera retornar uma excecao`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(null)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.extrato("-1")
    }

    @Test
    fun `O saldo da conta nunca podera ser negativo`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.saque(
            id = joaoAccount.id,
            valor = 100.00
        )

    }

    @Test
    fun `deve devolver saldo`() {

        joaoAccount.saldo = 200.00

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        val saldo = contaService.saldo(joaoAccount.id)
        assertEquals(200.00, saldo.saldo, 0.00001)
    }
}