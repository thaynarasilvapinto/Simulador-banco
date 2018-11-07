package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ClienteRepository
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ContaRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@RunWith(SpringRunner::class)
class JdbcContaRepositoryTest {
    @Autowired
    private lateinit var repositoryConta: ContaRepository

    private var accountId: Int = -1

    @Before
    fun setup() {
        accountId = saveAAccount()
    }

    @After
    fun tearDown() {
        repositoryConta.deleteById(accountId)
    }

    private fun saveAAccount(): Int {

        val account = Conta(
            id = 1,
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
            id = 2,
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
            id = 2,
            saldo = 0.00
        )
        repositoryConta.save(account)

        assertEquals(1, repositoryConta.deleteById(account.id))

    }
}