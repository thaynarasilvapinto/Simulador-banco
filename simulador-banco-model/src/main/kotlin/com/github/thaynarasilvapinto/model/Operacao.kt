package com.github.thaynarasilvapinto.model

import java.time.LocalDateTime
import java.util.*


data class Operacao(
    var idOperacao: String = UUID.randomUUID().toString(),
    var valorOperacao: Double,
    var dataHoraOperacao: LocalDateTime = LocalDateTime.now(),
    var tipoOperacao: TipoOperacao,
    var contaOrigem: Conta,
    var contaDestino: Conta
) {
    enum class TipoOperacao {
        SAQUE, DEPOSITO, TRANSFERENCIA, RECEBIMENTO_TRANSFERENCIA
    }

}