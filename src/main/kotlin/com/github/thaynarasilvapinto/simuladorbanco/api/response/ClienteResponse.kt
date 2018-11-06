package com.github.thaynarasilvapinto.simuladorbanco.api.response

import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class ClienteResponse(
        @NotNull
        val id: Int,
        @NotNull
        val nome: String,
        @NotNull
        val cpf: String,
        @NotNull
        val dataHora: LocalDateTime,
        @NotNull
        val conta: Int
)