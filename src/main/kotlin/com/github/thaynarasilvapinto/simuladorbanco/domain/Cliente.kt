package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.time.LocalDateTime
import java.util.*

data class Cliente(
    var id: String = UUID.randomUUID().toString(),
    var nome: String,
    var cpf: String,
    var dataHora: LocalDateTime = LocalDateTime.now(),
    var conta: Conta
)