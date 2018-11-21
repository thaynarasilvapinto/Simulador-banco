package com.github.thaynarasilvapinto.service

import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.model.Operacao
import com.github.thaynarasilvapinto.model.repository.ContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
open class ContaService {

    @Autowired
    private lateinit var repo: ContaRepository

    @Autowired
    private lateinit var serviceConta: ContaService

    @Autowired
    private lateinit var serviceOperacao: OperacaoService

    fun find(id: String): Optional<Conta> {
        return repo.findById(id)
    }

    fun insert(conta: Conta): Conta {
        repo.save(conta)
        return repo.findById(conta.id).get()
    }

    fun update(conta: Conta): Conta {
        find(conta.id)
        repo.update(conta)
        return repo.findById(conta.id).get()
    }

    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }


    fun extrato(id: String): List<Operacao> {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {

            emptyList<Operacao>()

            val recebimento = serviceOperacao.findAllByContaDestinoAndTipoOperacao(
                conta.get(),
                Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA
            )
            val trasferencia =
                serviceOperacao.findAllContaOrigemAndTipoOperacao(conta.get(), Operacao.TipoOperacao.TRANSFERENCIA)
            val deposito =
                serviceOperacao.findAllContaOrigemAndTipoOperacao(conta.get(), Operacao.TipoOperacao.DEPOSITO)
            val saque =
                serviceOperacao.findAllContaOrigemAndTipoOperacao(conta.get(), Operacao.TipoOperacao.SAQUE)


            var lista = recebimento + trasferencia + deposito + saque
            fun selector(p: Operacao): LocalDateTime = p.dataHoraOperacao
            lista = lista.sortedBy { selector(it) }
            return lista
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun saldo(id: String): Conta {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {
            return conta.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun conta(id: String): Conta {
        val conta = serviceConta.find(id)

        if (conta.isPresent) {
            return conta.get()
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }
}