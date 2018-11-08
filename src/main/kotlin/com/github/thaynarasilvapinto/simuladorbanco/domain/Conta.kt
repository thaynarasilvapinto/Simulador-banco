package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "conta")
data class Conta(
    @Id
    var id: String = UUID.randomUUID().toString(),
    @NotNull
    @Min(0)
    var saldo: Double,
    var dataHora: LocalDateTime = LocalDateTime.now()
)