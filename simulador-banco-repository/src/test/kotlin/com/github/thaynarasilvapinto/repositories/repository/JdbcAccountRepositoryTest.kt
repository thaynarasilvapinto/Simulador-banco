package com.github.thaynarasilvapinto.repositories.repository

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.repositories.config.RepositoryBaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JdbcAccountRepositoryTest : RepositoryBaseTest() {
    @Autowired
    private lateinit var repositoryAccount: AccountRepository

    private var accountId: String = "-1"

    @Before
    fun setup() {
        accountId = saveAAccount()
    }

    @After
    fun tearDown() {
        repositoryAccount.deleteById(accountId)
    }

    private fun saveAAccount(): String {

        val account = Account(
            saldo = 0.00
        )
        assertEquals(1, repositoryAccount.save(account))
        return account.id
    }


    @Test
    fun `deve encontrar uma conta ja criada`() {
        val account = repositoryAccount.findById(accountId)
        assertNotNull(account)
        assertEquals(accountId, account!!.id)
    }
    @Test
    fun `noa deve encontrar uma conta nao criada`() {
        val account = repositoryAccount.findById("-1")
        assertNull(account)
    }

    @Test
    fun `deve criar uma conta`() {
        val account = Account(
            saldo = 0.00
        )
        assertEquals(1, repositoryAccount.save(account))

        repositoryAccount.deleteById(account.id)
    }

    @Test
    fun `deve fazer um update em uma conta ja criada`() {
        var conta = repositoryAccount.findById(accountId)
        var contaEsperado = conta
        contaEsperado!!.saldo = 10.00

        repositoryAccount.update(contaEsperado)

        assertEquals(repositoryAccount.findById(accountId)!!.saldo, contaEsperado.saldo)
    }

    @Test
    fun `deve deletar em uma conta ja criado`() {

        val account = Account(
            saldo = 0.00
        )
        repositoryAccount.save(account)

        assertEquals(1, repositoryAccount.deleteById(account.id))

    }
}