package com.github.thaynarasilvapinto.simuladorbanco.services

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.OperacaoRepository
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.BalanceIsInsufficientException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class OperacaoService {

    @Autowired
    private lateinit var repo: OperacaoRepository

    @Autowired
    private lateinit var serviceConta: ContaService


    fun find(id: String): Optional<Operacao> {
        return repo.findById(id)
    }

    fun insert(obj: Operacao): Operacao {
        repo.save(obj)
        return repo.findById(obj.idOperacao).get()
    }


    fun delete(id: String) {
        find(id)
        repo.deleteById(id)
    }

    fun findAllContaOrigem(conta: Conta) = repo.findAllByContaOrigem(conta.id)

    fun findAllByContaDestinoAndTipoOperacao(conta: Conta, tipoOperacao: Operacao.TipoOperacao) =
        repo.findAllByContaDestinoAndTipoOperacao(conta.id, tipoOperacao.name)

    fun findAllContaOrigemAndTipoOperacao(conta: Conta, tipoOperacao: Operacao.TipoOperacao) =
        repo.findAllByContaOrigemAndTipoOperacao(conta.id, tipoOperacao.name)

    fun saque(valor: Double, id: String): Operacao {

        val conta = serviceConta.find(id)

        if (conta.isPresent) {
            if (valor <= conta.get().saldo) {

                var saque = Operacao(
                    contaOrigem = conta.get(),
                    contaDestino = conta.get(),
                    valorOperacao = valor,
                    tipoOperacao = Operacao.TipoOperacao.SAQUE
                )

                conta.get().saldo = conta.get().saldo - saque.valorOperacao

                serviceConta.update(conta.get())

                saque = insert(saque)

                return saque
            } else
                throw BalanceIsInsufficientException(message = "Saldo Insuficiente")
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun deposito(valor: Double, id: String): Operacao {

        val conta = serviceConta.find(id)

        if (conta.isPresent) {

            var deposito = Operacao(
                contaOrigem = conta.get(),
                contaDestino = conta.get(),
                valorOperacao = valor,
                tipoOperacao = Operacao.TipoOperacao.DEPOSITO
            )

            conta.get().saldo = conta.get().saldo + deposito.valorOperacao

            serviceConta.update(conta.get())

            deposito = insert(deposito)

            return deposito
        }
        throw AccountIsValidException(message = "A conta deve ser valida")
    }

    fun transferencia(valor: Double, id: String, idDestino: String): Operacao {

        val contaOrigem = serviceConta.find(id)
        val contaDestino = serviceConta.find(idDestino)

        if (id != idDestino) {
            if (contaOrigem.isPresent && contaDestino.isPresent) {
                if (valor <= contaOrigem.get().saldo) {

                    var recebimentoTransferencia = Operacao(
                        contaOrigem = contaOrigem.get(),
                        contaDestino = contaDestino.get(),
                        valorOperacao = valor,
                        tipoOperacao = Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA
                    )
                    var efetuarTrasferencia = Operacao(
                        contaOrigem = contaOrigem.get(),
                        contaDestino = contaDestino.get(),
                        valorOperacao = valor,
                        tipoOperacao = Operacao.TipoOperacao.TRANSFERENCIA
                    )


                    contaOrigem.get().saldo = contaOrigem.get().saldo - efetuarTrasferencia.valorOperacao
                    contaDestino.get().saldo = contaDestino.get().saldo + recebimentoTransferencia.valorOperacao

                    serviceConta.update(contaOrigem.get())
                    serviceConta.update(contaDestino.get())

                    efetuarTrasferencia = insert(efetuarTrasferencia)
                    insert(recebimentoTransferencia)

                    return efetuarTrasferencia

                } else
                    throw BalanceIsInsufficientException(message = "Saldo Insuficiente")
            }
            throw AccountIsValidException(message = "As contas devem ser validas")
        }
        throw AccountIsValidException(message = "Não pode efetuar uma transferencia para você mesmo")
    }
}