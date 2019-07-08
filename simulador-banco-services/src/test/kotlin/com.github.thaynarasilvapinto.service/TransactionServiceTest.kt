package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
import com.github.thaynarasilvapinto.service.config.ServiceBaseTest
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.BalanceIsInsufficientException
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Matchers.anyString
import kotlin.test.assertNotNull

class TransactionServiceTest : ServiceBaseTest() {

    @get:Rule
    var thrown = ExpectedException.none()


    private val repositoryAccount: AccountRepository = mock()
    private val repositoryTransaction: TransactionRepository = mock()

    private lateinit var operacaoService: OperacaoService
    private lateinit var contaService: ContaService

    private lateinit var joao: Cliente
    private lateinit var joaoAccount: Account
    private lateinit var transactionDepositoJoao: Transaction
    private lateinit var maria: Cliente
    private lateinit var accountMaria: Account

    @Before
    fun setup() {

        operacaoService = OperacaoService(repositoryTransaction, repositoryAccount)
        contaService = ContaService(repositoryAccount, repositoryTransaction)

        createClient()
        transactionDepositoJoao = Transaction(
            accountOrigem = joaoAccount,
            accountDestino = joaoAccount,
            valorOperacao = 200.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO
        )
    }

    private fun createClient() {
        joaoAccount = Account(saldo = 200.00)
        joao = Cliente(
            nome = "Account Test Joao Service",
            cpf = "151.425.426-75",
            conta = joaoAccount
        )

        accountMaria = Account(saldo = 0.00)
        maria = Cliente(
            nome = "Account Test Maria Service",
            cpf = "086.385.420-62",
            conta = accountMaria
        )
    }

    @Test
    fun buscar() {
        whenever(repositoryTransaction.findById(transactionDepositoJoao.idOperacao)).thenReturn(transactionDepositoJoao)
        operacaoService.find(transactionDepositoJoao.idOperacao)
        verify(repositoryTransaction, times(1)).findById(transactionDepositoJoao.idOperacao)
    }


    @Test
    fun `Nao pode se pode sacar um valor mais alto que o saldo`() {

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.saque(
            id = joaoAccount.id,
            valor = 1000000.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar transferencia quando o saldo na conta e insuficiente`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        thrown.expect(BalanceIsInsufficientException::class.java)
        thrown.expectMessage("Saldo Insuficiente")

        operacaoService.transferencia(
            id = joaoAccount.id,
            idDestino = accountMaria.id,
            valor = 10000.00
        )
    }

    @Test
    fun `Ao solicitar transferwncia tanto a conta de destino quanto a de origem devem ser validas`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

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
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("As contas devem ser validas")

        operacaoService.transferencia(
            id = "-1",
            idDestino = accountMaria.id,
            valor = 10.00
        )
    }

    @Test
    fun `Ao solicitar transferencia a conta de destino deve ser valida`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("As contas devem ser validas")

        operacaoService.transferencia(
            id = joaoAccount.id,
            idDestino = "-2",
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar uma transferencia para voce mesmo`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("Não pode efetuar uma transferencia para você mesmo")

        operacaoService.transferencia(
            id = joaoAccount.id,
            idDestino = joaoAccount.id,
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar um deposito quando a conta e invalida`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        operacaoService.deposito(
            id = "-1",
            valor = 100.00
        )
    }

    @Test
    fun `Nao pode ser possivel realizar um saque quando a conta e invalida`() {
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        thrown.expect(AccountIsValidException::class.java)
        thrown.expectMessage("A conta deve ser valida")

        operacaoService.saque(
            id = "-1",
            valor = 10.00
        )
    }

    @Test
    fun `deve realizar deposito`() {
        val deposito = Transaction(
            valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.DEPOSITO,
            accountDestino = joaoAccount,
            accountOrigem = joaoAccount
        )

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        joaoAccount.saldo = 100.00
        whenever(repositoryAccount.update(joaoAccount)).thenReturn(1)
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        whenever(repositoryTransaction.save(any())).thenReturn(1)
        whenever(repositoryTransaction.findById(anyString())).thenReturn(deposito)

        val resultDeposito = operacaoService.deposito(
            id = joaoAccount.id,
            valor = 100.00
        )

        assertNotNull(resultDeposito)
        assertEquals(deposito.idOperacao, resultDeposito.idOperacao)
    }

    @Test
    fun `deve realizar saque`() {
        val saque = Transaction(valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.SAQUE,
            accountDestino = joaoAccount,
            accountOrigem = joaoAccount)

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        joaoAccount.saldo = joaoAccount.saldo - 100.00
        whenever(repositoryAccount.update(joaoAccount)).thenReturn(1)
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        whenever(repositoryTransaction.save(any())).thenReturn(1)
        whenever(repositoryTransaction.findById(anyString())).thenReturn(saque)

        val resultSaque = operacaoService.saque(
            id = joaoAccount.id,
            valor = 100.00
        )
        assertNotNull(saque)
        assertEquals(saque.idOperacao, resultSaque!!.idOperacao)
    }
    @Test
    fun `deve realizar transferencia`() {
        val transferencia = Transaction(valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.TRANSFERENCIA,
            accountDestino = accountMaria,
            accountOrigem = joaoAccount)
        val transferenciaRecebimento = Transaction(valorOperacao = 100.00,
            tipoOperacao = Transaction.TipoOperacao.RECEBIMENTO_TRANSFERENCIA,
            accountDestino = accountMaria,
            accountOrigem = joaoAccount)

        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        joaoAccount.saldo = joaoAccount.saldo - 100.00
        whenever(repositoryAccount.update(joaoAccount)).thenReturn(1)
        whenever(repositoryAccount.findById(joaoAccount.id)).thenReturn(joaoAccount)

        accountMaria.saldo = accountMaria.saldo + 100.00
        whenever(repositoryAccount.update(accountMaria)).thenReturn(1)
        whenever(repositoryAccount.findById(accountMaria.id)).thenReturn(accountMaria)

        whenever(repositoryTransaction.save(any())).thenReturn(1)
        whenever(repositoryTransaction.findById(anyString())).thenReturn(transferenciaRecebimento)

        whenever(repositoryTransaction.save(any())).thenReturn(1)
        whenever(repositoryTransaction.findById(anyString())).thenReturn(transferencia)

        val resultTransferencia = operacaoService.transferencia(
            id = joaoAccount.id,
            idDestino = accountMaria.id,
            valor = 100.00
        )
        assertNotNull(transferencia)
        assertEquals(transferencia.idOperacao, resultTransferencia.idOperacao)
    }
}
