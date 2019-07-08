package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.repository.CustomerRepository
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.service.config.ServiceBaseTest
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.CpfIsValidException
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class ClienteServiceTest : ServiceBaseTest() {
    @get:Rule
    var thrown = ExpectedException.none()

    private val repositoryCustomer: CustomerRepository = mock()
    private val repositoryAccount: AccountRepository = mock()


    private lateinit var clienteService: ClienteService

    private lateinit var joao: Cliente
    private lateinit var joaoAccount: Account

    @Before
    fun setup() {
        clienteService = ClienteService(repositoryCustomer, repositoryAccount)
        createClient()
    }

    @Test
    fun `deve buscar um cliente`() {
        whenever(repositoryCustomer.findById(joao.id)).thenReturn(joao)

        val clienteBuscado = clienteService.cliente(joao.id)
        assertEquals(joao.id, clienteBuscado!!.id)
        verify(repositoryCustomer, times(1)).findById(joao.id)
    }
    @Test
    fun `nao deve buscar um cliente com conta invalida`() {
        whenever(repositoryCustomer.findById("-1")).thenReturn(null)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        clienteService.cliente("-1")
    }
    @Test
    fun update() {
        whenever(repositoryCustomer.findById(joao.id)).thenReturn(joao)
        joao.nome = "Client Test Update"
        whenever(repositoryCustomer.update(joao)).thenReturn(1)
        whenever(repositoryCustomer.findById(joao.id)).thenReturn(joao)

        joao = clienteService.update(joao)!!
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.nome, clienteBuscado!!.nome)

        verify(repositoryCustomer, times(1)).update(joao)
    }

    @Test
    fun `Ao criar a conta na resposta de sucesso devera constar o Id da conta para futuras movimentacoes`() {


        whenever(repositoryCustomer.findByCpfEquals(joao.cpf)).thenReturn(null)
        whenever(repositoryAccount.save(joaoAccount)).thenReturn(1)
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryCustomer.save(joao)).thenReturn(1)
        whenever(repositoryCustomer.findById(joao.id)).thenReturn(joao)


        val cliente = clienteService.criarCliente(joao)

        assertNotNull(cliente)
        assertEquals(joao, cliente)
    }

    @Test
    fun `O cliente so podera ter uma conta`() {
        whenever(repositoryCustomer.findByCpfEquals(joao.cpf)).thenReturn(joao)

        thrown.expect(CpfIsValidException::class.java)
        thrown.expectMessage("O CPF já existe")

        clienteService.criarCliente(Cliente(nome = "teste", cpf = "055.059.396-94", conta = Account(saldo = 0.00)))
    }

    private fun createClient() {

        joaoAccount = Account(saldo = 0.00)
        joao = Cliente(
            nome = "Cliente Test Account Controller",
            cpf = "055.059.396-94",
            conta = joaoAccount
        )
    }

}
