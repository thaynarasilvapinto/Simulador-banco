package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.repository.ClienteRepository
import com.github.thaynarasilvapinto.service.config.ServiceBaseTest
import com.github.thaynarasilvapinto.service.exception.CpfIsValidException
import com.nhaarman.mockito_kotlin.mock
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class ClienteServiceTest : ServiceBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()


    private val repository: ClienteRepository = mock()
    private val serviceConta: ContaService = mock()


    private lateinit var clienteService: ClienteService

    @Autowired
    private lateinit var contaService: ContaService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta

    @Before
    fun setup() {
        createClient()
        clienteService = ClienteService(repository, serviceConta)
    }

    private fun createClient() {

        joao = clienteService.criarCliente(
            Cliente(
                nome = "Cliente Test Conta Controller",
                cpf = "055.059.396-94",
                conta = Conta(saldo = 0.00)
            )
        )

        joaoConta = contaService.find(joao.conta.id).get()

    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        contaService.delete(joaoConta.id)
    }

    @Test
    fun `deve buscar um cliente`() {
        Mockito.`when`(repository.findById(joao.id)).thenReturn(Optional.of(joao))
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.id, clienteBuscado.get().id)
    }

    @Test
    fun update() {

        joao.nome = "Client Test Update"
        joao = clienteService.update(joao)
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.nome, clienteBuscado.get().nome)
    }

    @Test
    fun `Ao criar a conta na resposta de sucesso devera constar o Id da conta para futuras movimentacoes`() {
        var cliente = clienteService.criarCliente(
            Cliente(
                nome = "teste",
                cpf = "611.018.420-91",
                conta = Conta("-1", 0.00)
            )
        )
        val clienteEsperado = clienteService.find(cliente.id).get()
        assertNotNull(cliente.id)
        assertEquals(clienteEsperado, cliente)

        clienteService.delete(cliente.id)
        contaService.delete(cliente.conta.id)
    }

    @Test
    fun `O cliente so podera ter uma conta`() {
        thrown.expect(CpfIsValidException::class.java)
        thrown.expectMessage("O CPF já existe")

        clienteService.criarCliente(Cliente(nome = "teste", cpf = "055.059.396-94", conta = Conta(saldo = 0.00)))
    }

}