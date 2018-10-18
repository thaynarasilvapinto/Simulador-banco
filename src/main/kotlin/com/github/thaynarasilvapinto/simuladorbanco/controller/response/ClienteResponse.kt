package com.github.thaynarasilvapinto.simuladorbanco.controller.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import java.util.*

class ClienteResponse(
        @NotNull
        val id: Int?,
        @NotNull
        val nome: String?,
        @NotNull
        val cpf: String?,
        @NotNull
        val dataHora: LocalDateTime?,
        @NotNull
        val conta: ContaResponse?
){
        constructor(cliente: Cliente) : this(
                id = cliente.id,
                nome = cliente.nome,
                cpf = cliente.cpf,
                dataHora = cliente.dataHora,
                conta = ContaResponse(cliente.conta))
}
