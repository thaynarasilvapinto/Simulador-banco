package com.github.thaynarasilvapinto.simuladorbanco

//package com.github.thaynarasilvapinto.simuladorbanco;

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
class OperacaoServiceTest {

    @Autowired
    private lateinit var clienteService: ClienteService
    @Autowired
    private lateinit var contaService: ContaService
    @Autowired
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var operacaoDepositoJoao: Operacao

    @Before
    fun setup() {
        createClient()
        this.operacaoDepositoJoao = Operacao(
                contaDestino = joaoConta,
                contaOrigem = joaoConta,
                valorOperacao = 200.00,
                tipoOperacao = Operacao.TipoOperacao.DEPOSITO)

        operacaoDepositoJoao = operacaoService.insert(operacaoDepositoJoao)
        joaoConta.deposito(operacaoDepositoJoao)
        joaoConta = contaService.update(joaoConta)
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

    private fun createClient() {
        joaoConta = Conta(saldo = 0.00)
        joaoConta = contaService.insert(joaoConta)
        this.joao = Cliente(nome = "Cliente Test ClienteController", cpf = "151.425.426-75", conta = joaoConta)
        joao = clienteService.insert(joao)
    }

    @Test
    fun buscar() {
        val conta = operacaoService.find(operacaoDepositoJoao.idOperacao)
        val id = conta.get().idOperacao
        assertEquals(operacaoDepositoJoao.idOperacao, id)
    }
}

