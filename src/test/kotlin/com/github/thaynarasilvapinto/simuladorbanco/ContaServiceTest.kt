package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class ContaServiceTest {

    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta

    @Before
    fun setup() {
        createClient()
    }

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)
    }

    @After
    fun delete() {
        clienteService.delete(joao.id)
        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(joaoConta.id)
    }

    @Test
    fun buscar() {

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
    fun `deve buscar o saldo do cliente `() {
        val conta = contaService.find(joaoConta.id)
        val saldo = conta.get().saldo
        assertEquals(joaoConta.saldo, saldo, 0.00001)

    }

    @Test
    fun `deve buscar o extrato`() {

        val operacaoDepositoJoao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 100.00, tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
        val operacaoSaqueJoao = Operacao(contaOrigem = joaoConta, contaDestino = joaoConta, valorOperacao = 100.00, tipoOperacao = Operacao.TipoOperacao.SAQUE)

        var OperacaoDeposito = joao.conta.deposito(operacaoDepositoJoao)
        var OperacaoSaque = joao.conta.saque(operacaoSaqueJoao)

        OperacaoDeposito = operacaoService.insert(operacaoDepositoJoao)
        OperacaoSaque = operacaoService.insert(operacaoSaqueJoao)

        val extrato = operacaoService.findAllContaOrigem(joaoConta)

        assertEquals(Operacao.TipoOperacao.DEPOSITO, extrato[0].tipoOperacao)
        assertEquals(Operacao.TipoOperacao.SAQUE, extrato[1].tipoOperacao)
    }
}
