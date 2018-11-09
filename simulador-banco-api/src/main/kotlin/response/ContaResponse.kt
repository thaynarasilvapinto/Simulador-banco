package com.github.thaynarasilvapinto.api.response

import java.time.LocalDateTime

data class ContaResponse(
    val id: String,
    val saldo: Double,
    val dataHora: LocalDateTime
)