package com.github.thaynarasilvapinto.simuladorbanco.api.response

import java.time.LocalDateTime

data class ExtratoResponse(
        val idOperacao: String,
        val valorOperacao: Double,
        val dataHora: LocalDateTime,
        val tipoOperacao: TipoOperacao,
        val contaDestino: String
)