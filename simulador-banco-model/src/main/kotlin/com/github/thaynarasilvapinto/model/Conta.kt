package com.github.thaynarasilvapinto.model

import java.time.LocalDateTime
import java.util.*

data class Conta(
    var id: String = UUID.randomUUID().toString(),
    var saldo: Double,
    var dataHora: LocalDateTime = LocalDateTime.now()
)