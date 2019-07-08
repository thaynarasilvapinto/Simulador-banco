package com.github.thaynarasilvapinto.repositories.repository

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
import com.github.thaynarasilvapinto.repositories.config.RepositoryBaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JdbcTransactionTest : RepositoryBaseTest() {
    @Autowired
    private lateinit var repositoryAccount: AccountRepository
    @Autowired
    private lateinit var repositoryTransaction: TransactionRepository

    private var accountId: String = "-1"
    private var operacaoId: String = "-1"

    @Before
    fun setup() {
        operacaoId = saveACliente()
        accountId = repositoryAccount.findById(repositoryTransaction.findById(operacaoId)!!.accountOrigem.id)!!.id
    }

    @After
    fun tearDown() {
        repositoryTransaction.deleteById(operacaoId)
        repositoryAccount.deleteById(accountId)
    }

    private fun saveACliente(): String {

        val accountJoao = Account(
            saldo = 0.00
        )

        val operacaoDeposito = Transaction(
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO,
            accountOrigem = accountJoao,
            accountDestino = accountJoao
        )

        assertEquals(1, repositoryAccount.save(accountJoao))
        assertEquals(1, repositoryTransaction.save(operacaoDeposito))
        return operacaoDeposito.idOperacao
    }


    @Test
    fun `deve encontrar uma operacao ja criado pelo id`() {
        val operacao = repositoryTransaction.findById(operacaoId)
        assertNotNull(operacao)
        assertEquals(operacaoId, operacao!!.idOperacao)
    }
    @Test
    fun `nao deve encontrar uma operacao nao criada pelo id`() {
        val operacao = repositoryTransaction.findById("-1")
        assertNull(operacao)
    }

    @Test
    fun `deve criar uma operacao`() {
        val account = Account(
            saldo = 0.00
        )
        val operacaoDeposito = Transaction(
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO,
            accountOrigem = account,
            accountDestino = account
        )
        assertEquals(1, repositoryAccount.save(account))
        assertEquals(1, repositoryTransaction.save(operacaoDeposito))

        repositoryTransaction.deleteById(operacaoDeposito.idOperacao)
        repositoryAccount.deleteById(account.id)
    }


    @Test
    fun `deve deletar em uma operacao ja criada`() {

        val account = Account(
            saldo = 0.00
        )
        val operacaoDeposito = Transaction(
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO,
            accountOrigem = account,
            accountDestino = account
        )

        repositoryAccount.save(account)
        repositoryTransaction.save(operacaoDeposito)

        assertEquals(1, repositoryTransaction.deleteById(operacaoDeposito.idOperacao))
        assertEquals(1, repositoryAccount.deleteById(account.id))

    }

    @Test
    fun `deve encontrar uma operacao ja criado pelo conta de origem`() {

        var operacao = repositoryTransaction.findById(operacaoId)

        val operacaoListOrigem = repositoryTransaction.findAllByContaOrigem(operacao!!.accountOrigem.id)
        assertNotNull(operacaoListOrigem)
        assertEquals(operacao.idOperacao, operacaoListOrigem[0].idOperacao)
    }

    @Test
    fun `deve encontrar uma operacao ja criado pelo conta de destino e pelo tipo da operacao`() {

        var operacao = repositoryTransaction.findById(operacaoId)

        val operacaoListOrigem = repositoryTransaction.findAllByContaDestinoAndTipoOperacao(
            id = operacao!!.accountDestino.id,
            operacao = operacao.tipoOperacao.name
        )
        assertNotNull(operacaoListOrigem)
        assertEquals(operacao.idOperacao, operacaoListOrigem[0].idOperacao)
    }
}