package com.github.thaynarasilvapinto.simuladorbanco.domain

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "cliente")
data class Cliente(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = -1,
        @NotNull
        var nome: String,
        var cpf: String,
        var dataHora: LocalDateTime = LocalDateTime.now(),
        @OneToOne
        @JoinColumn(name = "conta_id")
        var conta: Conta
) : Serializable