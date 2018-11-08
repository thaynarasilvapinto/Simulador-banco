package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.BalanceIsInsufficientException
import org.junit.After
import org.junit.Assert.assertEquals
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
class ContaServiceTest {

    @get:Rule
    var thrown = ExpectedException.none()
    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var maria: Cliente
    private lateinit var contaMaria: Conta

    @Before
    fun setUp() {
        createClient()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)

        contaMaria = Conta(saldo = 0.00)
        contaMaria = contaService.insert(contaMaria)
        this.maria = Cliente(nome = "Cliente Test", cpf = "086.385.420-62", conta = contaMaria)
        maria = clienteService.insert(maria)
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        if(extrato != null){
            for (i in extrato!!.indices) {
                operacaoService.delete(extrato[i].idOperacao)
            }
        }
        contaService.delete(joaoConta.id)
    }

    @Test
    fun `deve buscar a conta`() {
        val conta = contaService.find(joaoConta.id)
        val id = conta.get().id
        assertEquals(joaoConta.id, id)
    }

    @Test
    fun update() {
        joaoConta.saldo = 100.00
        contaService.update(joaoConta)
        val conta = contaService.find(joaoConta.id)
        val saldo = conta.get().saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)
    }

    @Test
    fun `deve buscar o saldo`() {
        val conta = contaService.find(joaoConta.id)
        val saldo = conta.get().saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)

    }

    @Test
    fun `Ao solicitar um extrato, devera constar toda movimentacao da conta`() {

        operacaoService.deposito(
                id = joaoConta.id,
                valor = 200.00)
        operacaoService.saque(
                id = joaoConta.id,
                valor = 100.00)
        operacaoService.deposito(
                id = joaoConta.id,
                valor = 400.00)

        val extratoJoao = contaService.extrato(joaoConta.id)
        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[0].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.SAQUE, extratoJoao[1].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.DEPOSITO, extratoJoao[2].tipoOperacao)
    }

    @Test
    fun `Ao solicitar um extrato de uma conta invalida, devera retornar uma excecao`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        contaService.extrato("-1")
    }

    @Test
    fun `O saldo da conta nunca podera ser negativo`() {
        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")
        operacaoService.deposito(
                id = joaoConta.id,
                valor = 10.00)
        operacaoService.saque(
                id = joaoConta.id,
                valor = 100.00)

    }

    @Test
    fun `deve devolver saldo`() {
        operacaoService.deposito(
                id = joaoConta.id,
                valor = 200.00)
        operacaoService.saque(
                id = joaoConta.id,
                valor = 100.00)
        operacaoService.deposito(
                id = joaoConta.id,
                valor = 400.00)

        val saldo = contaService.saldo(joaoConta.id)

        assertEquals(500.00, saldo.saldo, 0.00001)
    }
}