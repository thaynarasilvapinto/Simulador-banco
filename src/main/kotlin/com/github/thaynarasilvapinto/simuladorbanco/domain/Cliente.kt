package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "cliente")
data class Cliente(
    @Id
    var id: String = UUID.randomUUID().toString(),
    @NotNull
    var nome: String,
    var cpf: String,
    var dataHora: LocalDateTime = LocalDateTime.now(),
    @OneToOne
    @JoinColumn(name = "conta_id")
    var conta: Conta
)