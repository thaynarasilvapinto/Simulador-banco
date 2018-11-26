package com.github.thaynarasilvapinto.api.response

import java.time.LocalDateTime

data class ClienteResponse(
    val id: String,
    val nome: String,
    val cpf: String,
    val dataHora: LocalDateTime,
    val conta: String
)