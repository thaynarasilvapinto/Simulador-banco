package com.github.thaynarasilvapinto.simuladorbanco.api.response

import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class ContaResponse(
        @NotNull
        val id: String,
        @NotNull
        val saldo: Double,
        @NotNull
        val dataHora: LocalDateTime
)