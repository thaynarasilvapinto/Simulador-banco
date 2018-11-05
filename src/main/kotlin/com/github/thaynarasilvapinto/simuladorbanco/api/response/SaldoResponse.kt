package com.github.thaynarasilvapinto.simuladorbanco.api.response

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta

class SaldoResponse(
        val saldo: Double
) {
    constructor(conta: Conta) : this(
            saldo = conta.saldo
    )
}