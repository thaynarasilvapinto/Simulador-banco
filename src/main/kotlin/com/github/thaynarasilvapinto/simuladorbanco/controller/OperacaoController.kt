package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.api.OperacaoApi
import com.github.thaynarasilvapinto.simuladorbanco.api.request.OperacaoRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.DepositoResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.SaqueResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.TransferenciaResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseDeposito
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseSaque
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseTransferencia
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
open class OperacaoController(
    private var serviceOperacao: OperacaoService
) : OperacaoApi {

    override fun saque(@PathVariable id: String, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<SaqueResponse> {
        val saque = serviceOperacao.saque(operacao.valorOperacao!!, id)
        return ResponseEntity.ok().body(saque.toResponseSaque())

    }

    override fun deposito(@PathVariable id: String, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<DepositoResponse> {
        val deposito = serviceOperacao.deposito(operacao.valorOperacao!!, id)
        return ResponseEntity.ok().body(deposito.toResponseDeposito())
    }

    override fun transferencia(@PathVariable id: String, @Valid @RequestBody operacaoTransferenciaRequest: OperacaoRequest): ResponseEntity<TransferenciaResponse> {
        val transferecia = serviceOperacao.transferencia(
            valor = operacaoTransferenciaRequest.valorOperacao!!,
            id = id,
            idDestino = operacaoTransferenciaRequest.contaDestino!!
        )
        return ResponseEntity.ok().body(transferecia.toResponseTransferencia())
    }
}