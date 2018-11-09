package com.github.thaynarasilvapinto.api.response

import com.github.thaynarasilvapinto.api.response.TipoOperacao
import java.time.LocalDateTime

data class TransferenciaResponse(
    val idOperacao: String,
    val valorOperacao: Double,
    val dataHora: LocalDateTime,
    val tipoOperacao: TipoOperacao,
    val contaDestino: String
)