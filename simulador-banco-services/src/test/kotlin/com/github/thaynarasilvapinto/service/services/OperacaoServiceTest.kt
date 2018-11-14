package com.github.thaynarasilvapinto.service.services

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.service.ClienteService
import com.github.thaynarasilvapinto.service.ContaService
import com.github.thaynarasilvapinto.service.OperacaoService
import com.github.thaynarasilvapinto.service.config.ServiceBaseTest
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.BalanceIsInsufficientException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired

class OperacaoServiceTest : ServiceBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()
    @InjectMocks
    private lateinit var clienteService: ClienteService
    @InjectMocks
    private lateinit var contaService: ContaService
    @InjectMocks
    private lateinit var operacaoService: OperacaoService
    private lateinit var joao: Cliente
    private lateinit var joaoConta: Conta
    private lateinit var operacaoDepositoJoao: Operacao
    private lateinit var maria: Cliente
    private lateinit var contaMaria: Conta

    @Before
    fun setUp() {
        createClient()
        operacaoDepositoJoao = operacaoService.deposito(
            id = joaoConta.id,
            valor = 200.00
        )
    }

    @After
    fun tearDown() {
        clienteService.delete(joao.id)

        val extrato = operacaoService.findAllContaOrigem(joaoConta)
        for (i in extrato.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }

        contaService.delete(joaoConta.id)

        clienteService.delete(maria.id)
        val extratoMaria = operacaoService.findAllContaOrigem(contaMaria)
        for (i in extratoMaria.indices) {
            operacaoService.delete(extrato[i].idOperacao)
        }
        contaService.delete(contaMaria.id)
    }

    private fun createClient() {
        joao = clienteService.criarCliente(
            Cliente(
                nome = "Conta Test Joao Service",
                cpf = "151.425.426-75",
                conta = Conta(saldo = 0.00)
            )
        )
        joaoConta = joao.conta
        maria = clienteService.criarCliente(
            Cliente(
                nome = "Conta Test Maria Service",
                cpf = "086.385.420-62",
                conta = Conta(saldo = 0.00)
            )
        )
        contaMaria = maria.conta
    }

    @Test
    fun buscar() {
        val conta = operacaoService.find(operacaoDepositoJoao.idOperacao)
        val id = conta.get().idOperacao
        assertEquals(operacaoDepositoJoao.idOperacao, id)
    }


    @Test
    fun `Nao pode se pode sacar um valor mais alto que o saldo`() {
        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.saque(
            id = joaoConta.id,
            valor = 1000000.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar transferencia quando o saldo na conta e insuficiente`() {
        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.transferencia(
            id = joaoConta.id,
            idDestino = contaMaria.id,
            valor = 10000.00
        )
    }

    @Test
    fun `Ao solicitar transferwncia tanto a conta de destino quanto a de origem devem ser validas`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("As contas devem ser validas")

        operacaoService.transferencia(
            id = "-1",
            idDestino = "-2",
            valor = 10.00
        )
    }

    @Test
    fun `Ao solicitar transferencia a conta de origem deve ser valida`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("As contas devem ser validas")

        operacaoService.transferencia(
            id = "-1",
            idDestino = contaMaria.id,
            valor = 10.00
        )
    }

    @Test
    fun `Ao solicitar transferencia a conta de destino deve ser valida`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("As contas devem ser validas")

        operacaoService.transferencia(
            id = joaoConta.id,
            idDestino = "-2",
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar uma transferencia para voce mesmo`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("Não pode efetuar uma transferencia para você mesmo")

        operacaoService.transferencia(
            id = joaoConta.id,
            idDestino = joaoConta.id,
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar um deposito quando a conta e invalida`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        operacaoService.deposito(
            id = "-1",
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar um saque quando a conta e invalida`() {
        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        operacaoService.saque(
            id = "-1",
            valor = 10.00
        )
    }

    @Test
    fun `deve realizar deposito`() {
        val deposito = operacaoService.deposito(
            id = joaoConta.id,
            valor = 100.00
        )

        val depositoNoBanco = operacaoService.find(deposito.idOperacao).get()
        assertNotNull(deposito)
        assertEquals(depositoNoBanco, deposito)
    }

    @Test
    fun `deve realizar saque`() {
        val saque = operacaoService.saque(
            id = joaoConta.id,
            valor = 100.00
        )
        val saqueNoBanco = operacaoService.find(saque.idOperacao).get()
        assertNotNull(saque)
        assertEquals(saqueNoBanco, saque)
    }

    @Test
    fun `deve realizar transferencia`() {
        val transferencia = operacaoService.transferencia(
            id = joaoConta.id,
            idDestino = contaMaria.id,
            valor = 100.00
        )
        val transferenciaNoBanco = operacaoService.find(transferencia.idOperacao).get()
        assertNotNull(transferencia)
        assertEquals(transferenciaNoBanco, transferencia)
    }
}
