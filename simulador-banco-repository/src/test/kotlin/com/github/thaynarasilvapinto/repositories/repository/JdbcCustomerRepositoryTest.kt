package com.github.thaynarasilvapinto.repositories.repository

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.repository.CustomerRepository
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.repositories.config.RepositoryBaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class JdbcCustomerRepositoryTest : RepositoryBaseTest() {
    @Autowired
    private lateinit var repositoryCustomer: CustomerRepository
    @Autowired
    private lateinit var repositoryAccount: AccountRepository

    private var clienteID: String = "-1"
    private var accountId: String = "-1"

    @Before
    fun setup() {
        clienteID = saveACliente()
        accountId = repositoryCustomer.findById(clienteID)!!.conta.id
    }

    @After
    fun tearDown() {
        repositoryCustomer.deleteById(clienteID)
        repositoryAccount.deleteById(accountId)
    }

    private fun saveACliente(): String {

        val account = Account(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Maitê Priscila Raimunda Moreira",
            cpf = "479.711.743-57", conta = account
        )
        assertEquals(1, repositoryAccount.save(account))
        assertEquals(1, repositoryCustomer.save(client))
        return client.id
    }

    @Test
    fun `deve encontrar um cliente ja criado`() {
        val cliente = repositoryCustomer.findById(clienteID)
        assertNotNull(cliente)
        assertEquals(clienteID, cliente!!.id)
    }
    @Test
    fun `nao deve encontrar um cliente que nao existe no banco`() {
        val cliente = repositoryCustomer.findById("-1")
        assertNull(cliente)
    }

    @Test
    fun `deve criar um cliente`() {
        val account = Account(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Sérgio Joaquim Silva",
            cpf = "134.191.446-10", conta = account
        )
        assertEquals(1, repositoryAccount.save(account))
        assertEquals(1, repositoryCustomer.save(client))

        repositoryCustomer.deleteById(client.id)
        repositoryAccount.deleteById(account.id)
    }

    @Test
    fun `deve fazer um update em um cliente ja criado`() {
        var cliente = repositoryCustomer.findById(clienteID)
        var clienteEsperado = cliente
        clienteEsperado!!.nome = "Cristiane Daiane Costa"

        repositoryCustomer.update(clienteEsperado)

        assertEquals(repositoryCustomer.findById(clienteID)!!.nome, clienteEsperado.nome)
    }

    @Test
    fun `deve deletar em um cliente ja criado`() {

        val account = Account(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Sérgio Joaquim Silva",
            cpf = "134.191.446-10", conta = account
        )

        repositoryAccount.save(account)
        repositoryCustomer.save(client)

        assertEquals(1, repositoryCustomer.deleteById(client.id))
        assertEquals(1, repositoryAccount.deleteById(account.id))

    }

    @Test
    fun `deve encontrar um cliente ja criado pelo cpf`() {
        val account = repositoryCustomer.findByCpfEquals("479.711.743-57")
        assertNotNull(account)
        assertEquals(clienteID, account!!.id)
    }
    @Test
    fun `nao deve encontrar um cliente ja criado pelo cpf`() {
        val account = repositoryCustomer.findByCpfEquals("479.711.743-5")
        assertNull(account)
    }
}
