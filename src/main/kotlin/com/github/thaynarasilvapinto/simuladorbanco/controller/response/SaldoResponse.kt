package com.github.thaynarasilvapinto.simuladorbanco.controller.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta

class SaldoResponse(
        val id: Int,
        val saldo: Double
) {
    constructor(conta: Conta) : this(
            id = conta.id,
            saldo = conta.saldo
    )
}