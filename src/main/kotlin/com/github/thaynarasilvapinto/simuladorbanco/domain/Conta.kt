package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "conta")
data class Conta(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = -1,
        @NotNull
        @Min(0)
        var saldo: Double,
        var dataHora: LocalDateTime = LocalDateTime.now()
) : Serializable {

    fun saque(operacao: Operacao): Operacao {
        if (operacao.valorOperacao <= this.saldo) {
            saldo -= operacao.valorOperacao
            return operacao
        }
        throw Exception("Saldo Insuficiente")
    }

    fun deposito(operacao: Operacao): Operacao {
        saldo += operacao.valorOperacao
        return operacao
    }

    fun recebimentoTransferencia(operacao: Operacao): Operacao {
        saldo += operacao.valorOperacao
        return operacao
    }

    fun efetuarTrasferencia(operacao: Operacao): Operacao {
        if (operacao.valorOperacao <= this.saldo) {
            saldo -= operacao.valorOperacao
            return operacao
        }
        throw Exception("Saldo Insuficiente")
    }
}