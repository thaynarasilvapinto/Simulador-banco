package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
open class ContaService(
    private var repo: AccountRepository,
    private var repoTransaction: TransactionRepository
) {


    fun find(id: String): Account? {
        return repo.findById(id)
    }

    fun update(account: Account): Account? {
        find(account.id)
        repo.update(account)
        return repo.findById(account.id)
    }

    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }


    fun extrato(id: String): List<Transaction> {
        val conta = find(id)

        if (conta != null) {

            emptyList<Transaction>()

            val recebimento = findAllByContaDestinoAndTipoOperacao(
                conta,
                Transaction.TipoOperacao.RECEBIMENTO_TRANSFERENCIA
            )
            val trasferencia = findAllContaOrigemAndTipoOperacao(
                conta,
                Transaction.TipoOperacao.TRANSFERENCIA
            )
            val deposito = findAllContaOrigemAndTipoOperacao(
                conta,
                Transaction.TipoOperacao.DEPOSITO
            )
            val saque = findAllContaOrigemAndTipoOperacao(
                conta,
                Transaction.TipoOperacao.SAQUE
            )


            var lista = recebimento + trasferencia + deposito + saque
            fun selector(p: Transaction): LocalDateTime = p.dataHoraOperacao
            lista = lista.sortedBy { selector(it) }
            return lista
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun saldo(id: String): Account {
        val conta = find(id)

        if (conta != null) {
            return conta
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun conta(id: String): Account {
        val conta = find(id)

        if (conta != null) {
            return conta
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun findAllByContaDestinoAndTipoOperacao(account: Account, tipoTransaction: Transaction.TipoOperacao) =
        repoTransaction.findAllByContaDestinoAndTipoOperacao(account.id, tipoTransaction.name)

    fun findAllContaOrigemAndTipoOperacao(account: Account, tipoTransaction: Transaction.TipoOperacao) =
        repoTransaction.findAllByContaOrigemAndTipoOperacao(account.id, tipoTransaction.name)
}