package com.github.thaynarasilvapinto.simuladorbanco.api.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class ContaResponse(
        @NotNull
        val id: Int?,
        @NotNull
        val saldo: Double?,
        @NotNull
        val dataHora: LocalDateTime?
        //TODO:Lembrar de perguntar o Danilo sobre a ?
){
    constructor(conta: Conta) : this (
            id = conta.id,
            saldo = conta.saldo,
            dataHora = conta.dataHora)
}
