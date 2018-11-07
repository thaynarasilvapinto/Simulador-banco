package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "operacao")
data class Operacao(
        @Id
        var idOperacao: Int = -1,
        var valorOperacao: Double,
        var dataHoraOperacao: LocalDateTime = LocalDateTime.now(),
        @Enumerated(EnumType.STRING)
        var tipoOperacao: TipoOperacao,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "conta_id_origem")
        var contaOrigem: Conta,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "conta_id_destino")
        var contaDestino: Conta
) : Serializable {
    enum class TipoOperacao {
        SAQUE, DEPOSITO, TRANSFERENCIA, RECEBIMENTO_TRANSFERENCIA
    }

}