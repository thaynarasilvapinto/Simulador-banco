package com.github.thaynarasilvapinto.simuladorbanco.api.response

import java.time.LocalDateTime

data class TransferenciaResponse(
        val idOperacao: Int,
        val valorOperacao: Double,
        val dataHora: LocalDateTime,
        val tipoOperacao: TipoOperacao,
        val contaDestino: Int
)