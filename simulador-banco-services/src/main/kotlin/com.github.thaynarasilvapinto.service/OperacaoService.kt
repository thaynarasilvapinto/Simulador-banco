package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.BalanceIsInsufficientException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class OperacaoService @Autowired constructor(
    private var repo: TransactionRepository,
    private var repoAccount: AccountRepository
) {


    fun find(id: String): Transaction? {
        return repo.findById(id)
    }

    fun insert(obj: Transaction): Transaction? {
        repo.save(obj)
        return repo.findById(obj.idOperacao)
    }


    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }

    fun findAllContaOrigem(account: Account) = repo.findAllByContaOrigem(account.id)

    fun saque(valor: Double, id: String): Transaction? {

        val conta = findConta(id)

        if (conta != null) {
            if (valor <= conta.saldo) {

                var saque = Transaction(
                    accountOrigem = conta,
                    accountDestino = conta,
                    valorOperacao = valor,
                    tipoOperacao = Transaction.TipoOperacao.SAQUE
                )

                conta.saldo = conta.saldo - saque.valorOperacao

                updateConta(conta)

                saque = insert(saque)!!

                return saque
            } else
                throw BalanceIsInsufficientException(message = "Saldo Insuficiente")
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun deposito(valor: Double, id: String): Transaction {

        val conta = findConta(id)

        if (conta != null) {

            var deposito = Transaction(
                accountOrigem = conta,
                accountDestino = conta,
                valorOperacao = valor,
                tipoOperacao = Transaction.TipoOperacao.DEPOSITO
            )

            conta.saldo = conta.saldo + deposito.valorOperacao

            updateConta(conta)

            deposito = insert(deposito)!!

            return deposito
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun transferencia(valor: Double, id: String, idDestino: String): Transaction {

        val contaOrigem = findConta(id)
        val contaDestino = findConta(idDestino)

        if (id != idDestino) {
            if (contaOrigem != null && contaDestino != null) {
                if (valor <= contaOrigem.saldo) {

                    var recebimentoTransferencia = Transaction(
                        accountOrigem = contaOrigem,
                        accountDestino = contaDestino,
                        valorOperacao = valor,
                        tipoOperacao = Transaction.TipoOperacao.RECEBIMENTO_TRANSFERENCIA
                    )
                    var efetuarTransferencia = Transaction(
                        accountOrigem = contaOrigem,
                        accountDestino = contaDestino,
                        valorOperacao = valor,
                        tipoOperacao = Transaction.TipoOperacao.TRANSFERENCIA
                    )

                    contaOrigem.saldo = contaOrigem.saldo - efetuarTransferencia.valorOperacao
                    contaDestino.saldo = contaDestino.saldo + recebimentoTransferencia.valorOperacao

                    updateConta(contaOrigem)
                    updateConta(contaDestino)

                    efetuarTransferencia = insert(efetuarTransferencia)!!
                    insert(recebimentoTransferencia)

                    return efetuarTransferencia

                } else
                    throw BalanceIsInsufficientException(message = "Saldo Insuficiente")
            }
            throw AccountIsValidException(message = "As contas devem ser validas")
        }
        throw AccountIsValidException(message = "Não pode efetuar uma transferencia para você mesmo")
    }


    fun findConta(id: String): Account? {
        return repoAccount.findById(id)
    }
    fun updateConta(account: Account): Account? {
        find(account.id)
        repoAccount.update(account)
        return repoAccount.findById(account.id)
    }
}