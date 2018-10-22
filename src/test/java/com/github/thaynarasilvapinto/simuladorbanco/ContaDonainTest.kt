package com.github.thaynarasilvapinto.simuladorbanco

//package com.github.thaynarasilvapinto.simuladorbanco;

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ContaDonainTest {

    private lateinit var joao: Cliente
    private lateinit var maria: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var mariaConta: Conta

    private lateinit var operacaoDepositoJoao: Operacao
    private lateinit var operacaoSaqueJoao: Operacao
    private lateinit var operacaoEfetuaTransferencia: Operacao
    private lateinit var operacaorecebeTransferencia: Operacao

    @Before
    fun setup() {
        mariaConta = Conta(saldo = 0.00)
        joaoConta = Conta(saldo = 0.00)
        this.joao = Cliente(nome = "João Pedro da Silva", cpf = "151.425.426-75", conta = joaoConta)
        this.maria = Cliente(nome = "Maria Abadia de Oliveira", cpf = "177.082.896-67", conta = mariaConta)


        this.operacaoDepositoJoao = Operacao(
                contaOrigem = joaoConta,
                contaDestino = joaoConta,
                valorOperacao = 200.00,
                tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
        this.operacaoSaqueJoao = Operacao(
                contaOrigem = joaoConta,
                contaDestino = joaoConta,
                valorOperacao = 100.00,
                tipoOperacao = Operacao.TipoOperacao.SAQUE)
        this.operacaoEfetuaTransferencia = Operacao(
                contaOrigem = joaoConta,
                contaDestino = mariaConta,
                valorOperacao = 100.00,
                tipoOperacao = Operacao.TipoOperacao.TRANSFERENCIA)
        this.operacaorecebeTransferencia = Operacao(
                contaOrigem = joaoConta,
                contaDestino = mariaConta,
                valorOperacao = 100.00,
                tipoOperacao = Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA)
    }

    @Test
    fun `deve realizar deposito`() {
        joao.conta.deposito(operacaoDepositoJoao)

        val saldoEsperado = 200.00
        val saldoAtual = joao.conta.saldo

        assertEquals(saldoEsperado, saldoAtual, 0.00001)
    }

    @Test
    fun `deve realizar saque`() {
        val saldoEsperado = 100.00
        val saldoAtual: Double

        joao.conta.deposito(operacaoDepositoJoao)
        joao.conta.saque(operacaoSaqueJoao)

        saldoAtual = joao.conta.saldo

        assertEquals(saldoEsperado, saldoAtual, 0.00001)
    }


    @Test
    fun `deve realizar transferencia`() {
        val saldoEsperadoJoao = 100.0
        val saldoEsperadoMaria = 100.0
        val saldoAtualJoao: Double
        val saldoAtualMaria: Double
        joao.conta.saldo = 200.00
        joao.conta.efetuarTrasferencia(operacaoEfetuaTransferencia)
        maria.conta.recebimentoTransferencia(operacaorecebeTransferencia)

        assertEquals(saldoEsperadoJoao, joao.conta.saldo, 0.00001)
        assertEquals(saldoEsperadoMaria, maria.conta.saldo, 0.00001)
    }
}
