package com.github.thaynarasilvapinto.simuladorbanco.services

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ContaService {

    @Autowired
    private lateinit var repo: ContaRepository

    @Autowired
    private lateinit var serviceConta: ContaService

    @Autowired
    private lateinit var serviceOperacao: OperacaoService

    fun find(id: Int): Optional<Conta> {
        return repo.findById(id)
    }

    fun insert(obj: Conta) = repo.findById(repo.save(obj)).get()

    fun update(obj: Conta): Conta {
        find(obj.id)
        return repo.findById(repo.save(obj)).get()
    }

    fun delete(id: Int) {
        find(id)
        repo.deleteById(id)
    }


    fun extrato(id: Int): List<Operacao> {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {

            emptyList<Operacao>()

            val recebimento = serviceOperacao.findAllByContaDestinoAndTipoOperacao(conta.get(), Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA)
            val trasferencia = serviceOperacao.findAllByContaDestinoAndTipoOperacao(conta.get(), Operacao.TipoOperacao.TRANSFERENCIA)
            val deposito = serviceOperacao.findAllByContaDestinoAndTipoOperacao(conta.get(), Operacao.TipoOperacao.DEPOSITO)
            val saque = serviceOperacao.findAllByContaDestinoAndTipoOperacao(conta.get(), Operacao.TipoOperacao.SAQUE)

            var lista = recebimento + trasferencia + deposito + saque
            fun selector(p: Operacao): Int = p.idOperacao
            lista = lista.sortedBy { selector(it) }
            return lista
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun saldo(id: Int): Conta {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {
            return conta.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun conta(id: Int): Conta {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {
            return conta.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }
}