package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "operacao")
data class Operacao(
    @Id
    var idOperacao: String = UUID.randomUUID().toString(),
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
) {
    enum class TipoOperacao {
        SAQUE, DEPOSITO, TRANSFERENCIA, RECEBIMENTO_TRANSFERENCIA
    }

}