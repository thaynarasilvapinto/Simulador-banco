package com.github.thaynarasilvapinto.simuladorbanco

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.OperacaoRepository
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
class JdbcOperacaoTest {
    @Autowired
    private lateinit var repositoryConta: ContaRepository
    @Autowired
    private lateinit var repositoryOperacao: OperacaoRepository

    private var accountId: Int = -1
    private var operacaoId: Int = -1

    @Before
    fun setup() {
        operacaoId = saveACliente()
        accountId = repositoryConta.findById(repositoryOperacao.findById(operacaoId).get().contaOrigem.id).get().id
    }

    @After
    fun tearDown() {
        repositoryOperacao.deleteById(operacaoId)
        repositoryConta.deleteById(accountId)
    }

    private fun saveACliente(): Int {

        val accountJoao = Conta(
            id = 1,
            saldo = 0.00
        )

        val operacaoDeposito = Operacao(
            idOperacao = 1,
            valorOperacao = 100.00,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO,
            contaOrigem = accountJoao,
            contaDestino = accountJoao
        )

        assertEquals(1, repositoryConta.save(accountJoao))
        assertEquals(1, repositoryOperacao.save(operacaoDeposito))
        return operacaoDeposito.idOperacao
    }


    @Test
    fun `deve encontrar uma operacao ja criado pelo id`() {
        val operacao = repositoryOperacao.findById(operacaoId)
        assertNotNull(operacao)
        assertEquals(operacaoId, operacao.get().idOperacao)
    }

    @Test
    fun `deve criar uma operacao`() {
        val account = Conta(
            id = 2,
            saldo = 0.00
        )
        val operacaoDeposito = Operacao(
            idOperacao = 2,
            valorOperacao = 100.00,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO,
            contaOrigem = account,
            contaDestino = account
        )
        assertEquals(1, repositoryConta.save(account))
        assertEquals(1, repositoryOperacao.save(operacaoDeposito))

        repositoryOperacao.deleteById(operacaoDeposito.idOperacao)
        repositoryConta.deleteById(account.id)
    }


    @Test
    fun `deve deletar em uma operacao ja criada`() {

        val account = Conta(
            id = 2,
            saldo = 0.00
        )
        val operacaoDeposito = Operacao(
            idOperacao = 2,
            valorOperacao = 100.00,
            tipoOperacao = Operacao.TipoOperacao.DEPOSITO,
            contaOrigem = account,
            contaDestino = account
        )

        repositoryConta.save(account)
        repositoryOperacao.save(operacaoDeposito)

        assertEquals(1, repositoryOperacao.deleteById(operacaoDeposito.idOperacao))
        assertEquals(1, repositoryConta.deleteById(account.id))

    }

    @Test
    fun `deve encontrar uma operacao ja criado pelo conta de origem`(){

        var operacao = repositoryOperacao.findById(operacaoId)

        val operacaoListOrigem = repositoryOperacao.findAllByContaOrigem(operacao.get().contaOrigem.id)
        assertNotNull(operacaoListOrigem)
        assertEquals(operacao.get().idOperacao, operacaoListOrigem[0].idOperacao)
    }

    @Test
    fun `deve encontrar uma operacao ja criado pelo conta de destino e pelo tipo da operacao`(){

        var operacao = repositoryOperacao.findById(operacaoId)

        val operacaoListOrigem = repositoryOperacao.findAllByContaDestinoAndTipoOperacao(
            id = operacao.get().contaDestino.id,
            operacao = operacao.get().tipoOperacao.name)
        assertNotNull(operacaoListOrigem)
        assertEquals(operacao.get().idOperacao, operacaoListOrigem[0].idOperacao)
    }
}