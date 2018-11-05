package com.github.thaynarasilvapinto.simuladorbanco.controller.utils

import com.github.thaynarasilvapinto.simuladorbanco.api.response.*
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao

fun Cliente.toResponse(): ClienteResponse =
        ClienteResponse(
                id = this.id,
                cpf = this.cpf,
                nome = this.nome,
                dataHora = this.dataHora,
                conta = this.conta.toResponse()
        )

fun Conta.toResponse(): ContaResponse =
        ContaResponse(
                id = this.id,
                saldo = this.saldo,
                dataHora = this.dataHora
        )

fun Operacao.toResponseDeposito(): DepositoResponse =
        DepositoResponse(
                idOperacao = this.idOperacao,
                valorOperacao = this.valorOperacao,
                dataHora = this.dataHoraOperacao,
                tipoOperacao = enumValueOf(this.tipoOperacao.name)
        )

fun Operacao.toResponseSaque(): SaqueResponse =
        SaqueResponse(
                idOperacao = this.idOperacao,
                valorOperacao = this.valorOperacao,
                dataHora = this.dataHoraOperacao,
                tipoOperacao = enumValueOf(this.tipoOperacao.name)
        )

fun Operacao.toResponseTransferencia(): TransferenciaResponse =
        TransferenciaResponse(
                idOperacao = this.idOperacao,
                valorOperacao = this.valorOperacao,
                dataHora = this.dataHoraOperacao,
                tipoOperacao = enumValueOf(this.tipoOperacao.name),
                contaDestino = this.contaDestino.id
        )

fun Conta.toResponseSaldo(): SaldoResponse =
        SaldoResponse(
                saldo = this.saldo
        )