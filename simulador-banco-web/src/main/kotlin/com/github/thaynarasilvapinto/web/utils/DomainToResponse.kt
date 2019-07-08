package com.github.thaynarasilvapinto.web.utils

import com.github.thaynarasilvapinto.api.response.*
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction

fun Cliente.toResponse(): ClienteResponse =
    ClienteResponse(
        id = this.id,
        cpf = this.cpf,
        nome = this.nome,
        dataHora = this.dataHora,
        conta = this.conta.id
    )

fun Account.toResponse(): ContaResponse =
    ContaResponse(
        id = this.id,
        saldo = this.saldo,
        dataHora = this.dataHora
    )

fun Transaction.toResponseDeposito(): DepositoResponse =
    DepositoResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name)
    )

fun Transaction.toResponseSaque(): SaqueResponse =
    SaqueResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name)
    )

fun Transaction.toResponseTransferencia(): TransferenciaResponse =
    TransferenciaResponse(
        idOperacao = this.idOperacao,
        valorOperacao = this.valorOperacao,
        dataHora = this.dataHoraOperacao,
        tipoOperacao = enumValueOf(this.tipoOperacao.name),
        contaDestino = this.accountDestino.id
    )

fun Account.toResponseSaldo(): SaldoResponse =
    SaldoResponse(
        saldo = this.saldo
    )