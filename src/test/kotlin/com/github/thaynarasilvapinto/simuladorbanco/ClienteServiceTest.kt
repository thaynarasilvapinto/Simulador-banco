package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.CpfIsValidException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class ClienteServiceTest {

    @get:Rule
    var thrown = ExpectedException.none()
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta

    @Before
    fun setUp() {
        createClient()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        contaService.delete(joaoConta.id)
    }

    @Test
    fun `deve buscar um cliente`() {
        val clienteBuscado = clienteService.find(joao.id)
        assertEquals(joao.id.toLong(), clienteBuscado.get().id.toLong())
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
        var cliente = clienteService.criarCliente(Cliente(
                nome = "teste",
                cpf = "611.018.420-91",
                conta = Conta(-1, 0.00)))
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

        clienteService.criarCliente(Cliente(nome = "teste", cpf = "151.425.426-75", conta = Conta(-1, 0.00)))
    }

}