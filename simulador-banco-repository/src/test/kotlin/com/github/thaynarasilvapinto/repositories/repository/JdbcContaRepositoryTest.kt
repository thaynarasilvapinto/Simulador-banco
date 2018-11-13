package com.github.thaynarasilvapinto.repositories.repository

import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.repository.ContaRepository
import com.github.thaynarasilvapinto.repositories.config.RepositoryBaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JdbcContaRepositoryTest : RepositoryBaseTest() {
    @Autowired
    private lateinit var repositoryConta: ContaRepository

    private var accountId: String = "-1"

    @Before
    fun setup() {
        accountId = saveAAccount()
    }

    @After
    fun tearDown() {
        repositoryConta.deleteById(accountId)
    }

    private fun saveAAccount(): String {

        val account = Conta(
            saldo = 0.00
        )
        assertEquals(1, repositoryConta.save(account))
        return account.id
    }


    @Test
    fun `deve encontrar uma conta ja criada`() {
        val account = repositoryConta.findById(accountId)
        assertNotNull(account)
        assertEquals(accountId, account.get().id)
    }

    @Test
    fun `deve criar uma conta`() {
        val account = Conta(
            saldo = 0.00
        )
        assertEquals(1, repositoryConta.save(account))

        repositoryConta.deleteById(account.id)
    }

    @Test
    fun `deve fazer um update em uma conta ja criada`() {
        var conta = repositoryConta.findById(accountId)
        var contaEsperado = conta
        contaEsperado.get().saldo = 10.00

        repositoryConta.update(contaEsperado.get())

        assertEquals(repositoryConta.findById(accountId).get().saldo, contaEsperado.get().saldo)
    }

    @Test
    fun `deve deletar em uma conta ja criado`() {

        val account = Conta(
            saldo = 0.00
        )
        repositoryConta.save(account)

        assertEquals(1, repositoryConta.deleteById(account.id))

    }
}