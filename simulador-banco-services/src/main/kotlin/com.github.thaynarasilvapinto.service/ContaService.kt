package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.model.repository.ContaRepository
import com.github.thaynarasilvapinto.model.repository.OperacaoRepository
import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
open class ContaService(
    private var repo: ContaRepository,
    private var repoOperacao: OperacaoRepository
) {


    fun find(id: String): Conta? {
        return repo.findById(id)
    }

    fun update(conta: Conta): Conta? {
        find(conta.id)
        repo.update(conta)
        return repo.findById(conta.id)
    }

    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }


    fun extrato(id: String): List<Operacao> {
        val conta = find(id)

        if (conta != null) {

            emptyList<Operacao>()

            val recebimento = findAllByContaDestinoAndTipoOperacao(
                conta,
                Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA
            )
            val trasferencia = findAllContaOrigemAndTipoOperacao(
                conta,
                Operacao.TipoOperacao.TRANSFERENCIA
            )
            val deposito = findAllContaOrigemAndTipoOperacao(
                conta,
                Operacao.TipoOperacao.DEPOSITO
            )
            val saque = findAllContaOrigemAndTipoOperacao(
                conta,
                Operacao.TipoOperacao.SAQUE
            )


            var lista = recebimento + trasferencia + deposito + saque
            fun selector(p: Operacao): LocalDateTime = p.dataHoraOperacao
            lista = lista.sortedBy { selector(it) }
            return lista
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun saldo(id: String): Conta {
        val conta = find(id)

        if (conta != null) {
            return conta
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun conta(id: String): Conta {
        val conta = find(id)

        if (conta != null) {
            return conta
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun findAllByContaDestinoAndTipoOperacao(conta: Conta, tipoOperacao: Operacao.TipoOperacao) =
        repoOperacao.findAllByContaDestinoAndTipoOperacao(conta.id, tipoOperacao.name)

    fun findAllContaOrigemAndTipoOperacao(conta: Conta, tipoOperacao: Operacao.TipoOperacao) =
        repoOperacao.findAllByContaOrigemAndTipoOperacao(conta.id, tipoOperacao.name)
}