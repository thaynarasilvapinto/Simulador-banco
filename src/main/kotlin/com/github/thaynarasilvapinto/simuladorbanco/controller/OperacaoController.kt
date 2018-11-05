package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.api.request.OperacaoRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.DepositoResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.SaqueResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.TransferenciaResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseDeposito
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseSaque
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseTransferencia
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping
open class OperacaoController {

    @Autowired
    private lateinit var serviceOperacao: OperacaoService


    @PostMapping(value = ["/conta/{id}/saque"])
    fun saque(@PathVariable id: Int, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<SaqueResponse> {
        val saque = serviceOperacao.saque(operacao.valorOperacao!!, id)
        return ResponseEntity.ok().body(saque.toResponseSaque())

    }

    @PostMapping(value = ["/conta/{id}/deposito"])
    fun deposito(@PathVariable id: Int, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<DepositoResponse> {
        val deposito = serviceOperacao.deposito(operacao.valorOperacao!!, id)
        return ResponseEntity.ok().body(deposito.toResponseDeposito())
    }

    @PostMapping(value = ["/conta/{id}/transferencia"])
    fun transferencia(@PathVariable id: Int, @Valid @RequestBody operacaoTransferenciaRequest: OperacaoRequest): ResponseEntity<TransferenciaResponse> {
        val transferecia = serviceOperacao.transferencia(valor = operacaoTransferenciaRequest.valorOperacao!!, id = id, idDestino = operacaoTransferenciaRequest.contaDestino!!)
        return ResponseEntity.ok().body(transferecia.toResponseTransferencia())
    }
}