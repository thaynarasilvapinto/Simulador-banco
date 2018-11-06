package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "conta")
data class Conta(
        @Id
        var id: Int = -1,
        @NotNull
        @Min(0)
        var saldo: Double,
        var dataHora: LocalDateTime = LocalDateTime.now()
) : Serializable