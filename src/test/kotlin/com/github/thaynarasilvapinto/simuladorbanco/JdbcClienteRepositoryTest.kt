package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
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
class JdbcClienteRepositoryTest {
    @Autowired
    private lateinit var repositoryCliente: ClienteRepository
    @Autowired
    private lateinit var repositoryConta: ContaRepository

    private var clienteID: Int = -1
    private var accountId: Int = -1

    @Before
    fun setup() {
        clienteID = saveACliente()
        accountId = repositoryCliente.findById(clienteID).get().conta.id
    }

    @After
    fun tearDown() {
        repositoryCliente.deleteById(clienteID)
        repositoryConta.deleteById(accountId)
    }

    private fun saveACliente(): Int {

        val account = Conta(
            id = 1,
            saldo = 0.00
        )
        val client = Cliente(
            id = 1,
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
        assertEquals(clienteID, cliente.get().id)
    }

    @Test
    fun `deve criar um cliente`() {
        val account = Conta(
            id = 2,
            saldo = 0.00
        )
        val client = Cliente(
            id = 2,
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
        clienteEsperado.get().nome = "Cristiane Daiane Costa"

        repositoryCliente.update(clienteEsperado.get())

        assertEquals(repositoryCliente.findById(clienteID).get().nome, clienteEsperado.get().nome)
    }

    @Test
    fun `deve deletar em um cliente ja criado`() {

        val account = Conta(
            id = 2,
            saldo = 0.00
        )
        val client = Cliente(
            id = 2,
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
        assertEquals(clienteID, account.get().id)
    }
}
