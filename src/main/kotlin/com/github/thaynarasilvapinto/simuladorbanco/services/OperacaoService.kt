package com.github.thaynarasilvapinto.simuladorbanco.services

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.repositories.OperacaoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.swing.SwingUtilities


@Service
open class OperacaoService {

    @Autowired
    private lateinit var repo: OperacaoRepository

    fun find(id: Int): Optional<Operacao>{
        return repo.findById(id)
    }

    fun insert(obj: Operacao) = repo.save(obj)

    fun update(obj: Operacao): Operacao {
        find(obj.idOperacao)
        return repo.save(obj)
    }

    fun delete(id: Int) {
        find(id)
        repo.deleteById(id)
    }

    fun findAllContaOrigem(conta: Conta) = repo.findAllByContaOrigem(conta)

    fun extrato(conta: Conta): List<Operacao> {

        emptyList<Operacao>()

        val recebimento = repo.findAllByContaDestinoAndTipoOperacao(conta, Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA)
        val trasferencia = repo.findAllByContaOrigemAndTipoOperacao(conta, Operacao.TipoOperacao.TRANSFERENCIA)
        val deposito = repo.findAllByContaOrigemAndTipoOperacao(conta, Operacao.TipoOperacao.DEPOSITO)
        val saque = repo.findAllByContaOrigemAndTipoOperacao(conta, Operacao.TipoOperacao.SAQUE)

        var extrato = recebimento + trasferencia + deposito + saque
        fun selector(p: Operacao): Int = p.idOperacao
        extrato = extrato.sortedBy {selector(it)}
        return extrato
    }
}