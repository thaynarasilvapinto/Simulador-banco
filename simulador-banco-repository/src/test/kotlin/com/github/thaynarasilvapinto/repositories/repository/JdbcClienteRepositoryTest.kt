package com.github.thaynarasilvapinto.repositories.repository

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.repository.ClienteRepository
import com.github.thaynarasilvapinto.model.repository.ContaRepository
import com.github.thaynarasilvapinto.repositories.config.RepositoryBaseTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class JdbcClienteRepositoryTest : RepositoryBaseTest() {
    @Autowired
    private lateinit var repositoryCliente: ClienteRepository
    @Autowired
    private lateinit var repositoryConta: ContaRepository

    private var clienteID: String = "-1"
    private var accountId: String = "-1"

    @Before
    fun setup() {
        clienteID = saveACliente()
        accountId = repositoryCliente.findById(clienteID)!!.conta.id
    }

    @After
    fun tearDown() {
        repositoryCliente.deleteById(clienteID)
        repositoryConta.deleteById(accountId)
    }

    private fun saveACliente(): String {

        val account = Conta(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Maitê Priscila Raimunda Moreira",
            cpf = "479.711.743-57", conta = account
        )
        assertEquals(1, repositoryConta.save(account))
        assertEquals(1, repositoryCliente.save(client))
        return client.id
    }

    @Test
    fun `deve encontrar um cliente ja criado`() {
        val cliente = repositoryCliente.findById(clienteID)
        assertNotNull(cliente)
        assertEquals(clienteID, cliente!!.id)
    }
    @Test
    fun `nao deve encontrar um cliente que nao existe no banco`() {
        val cliente = repositoryCliente.findById("-1")
        assertNull(cliente)
    }

    @Test
    fun `deve criar um cliente`() {
        val account = Conta(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Sérgio Joaquim Silva",
            cpf = "134.191.446-10", conta = account
        )
        assertEquals(1, repositoryConta.save(account))
        assertEquals(1, repositoryCliente.save(client))

        repositoryCliente.deleteById(client.id)
        repositoryConta.deleteById(account.id)
    }

    @Test
    fun `deve fazer um update em um cliente ja criado`() {
        var cliente = repositoryCliente.findById(clienteID)
        var clienteEsperado = cliente
        clienteEsperado!!.nome = "Cristiane Daiane Costa"

        repositoryCliente.update(clienteEsperado!!)

        assertEquals(repositoryCliente.findById(clienteID)!!.nome, clienteEsperado.nome)
    }

    @Test
    fun `deve deletar em um cliente ja criado`() {

        val account = Conta(
            saldo = 0.00
        )
        val client = Cliente(
            nome = "Sérgio Joaquim Silva",
            cpf = "134.191.446-10", conta = account
        )

        repositoryConta.save(account)
        repositoryCliente.save(client)

        assertEquals(1, repositoryCliente.deleteById(client.id))
        assertEquals(1, repositoryConta.deleteById(account.id))

    }

    @Test
    fun `deve encontrar um cliente ja criado pelo cpf`() {
        val account = repositoryCliente.findByCpfEquals("479.711.743-57")
        assertNotNull(account)
        assertEquals(clienteID, account!!.id)
    }
    @Test
    fun `nao deve encontrar um cliente ja criado pelo cpf`() {
        val account = repositoryCliente.findByCpfEquals("479.711.743-5")
        assertNull(account)
    }
}
