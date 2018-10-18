package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.controller.request.OperacaoDepositoRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.request.OperacaoSaqueRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.request.OperacaoTransferenciaRequest
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping(value = "/")
open class OperacaoController {
    @Autowired
    private lateinit var serviceConta: ContaService
    @Autowired
    private lateinit var serviceOperacao: OperacaoService


    @GetMapping(value = "/operacao/{id}")
    fun find(@PathVariable id: Int?): ResponseEntity<OperacaoResponse> {
        val operacao = serviceOperacao.find(id)
        return if (operacao != null) ResponseEntity.ok().body(OperacaoResponse(operacao)) else ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/conta/{id}/saque")
    fun saque(@PathVariable id: Int?, @Valid @RequestBody operacao: OperacaoSaqueRequest): ResponseEntity<OperacaoResponse> {
        val conta = serviceConta.find(id)

        if (conta != null) {
            if (operacao.valorOperacao!! > 0) {
                var saque: Operacao? = Operacao(conta, conta, operacao.valorOperacao!!, TipoOperacao.SAQUE)
                saque = conta.saque(saque!!)
                if (saque != null) {
                    serviceConta.update(conta)
                    serviceOperacao.insert(saque)
                    return ResponseEntity.ok().body(OperacaoResponse(saque))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/conta/{id}/deposito")
    fun deposito(@PathVariable id: Int?, @Valid @RequestBody operacao: OperacaoDepositoRequest): ResponseEntity<OperacaoResponse> {
        val conta = serviceConta.find(id)
        if (conta != null) {
            if (operacao.valorOperacao!! > 0) {
                var deposito: Operacao? = Operacao(conta, conta, operacao.valorOperacao!!, TipoOperacao.DEPOSITO)
                deposito = conta.deposito(deposito!!)
                if (deposito != null) {
                    serviceConta.update(conta)
                    serviceOperacao.insert(deposito)
                    return ResponseEntity.ok().body(OperacaoResponse(deposito))
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/conta/{id}/transferencia")
    fun transferencia(@PathVariable id: Int?, @Valid @RequestBody operacaoTransferenciaRequest: OperacaoTransferenciaRequest): ResponseEntity<OperacaoResponse> {

        val contaOrigem = serviceConta.find(id)
        val contaDestino = serviceConta.find(operacaoTransferenciaRequest.contaDestino)


        contaDestino?.id ?: "adadad"

        if (id !== operacaoTransferenciaRequest.contaDestino)
            if (contaDestino != null && contaOrigem != null) {

                var efetuarTrasferencia: Operacao? = Operacao(contaDestino, contaOrigem, operacaoTransferenciaRequest.valorOperacao, TipoOperacao.TRANSFERENCIA)
                var receberTransferencia: Operacao? = Operacao(contaDestino, contaOrigem, operacaoTransferenciaRequest.valorOperacao, TipoOperacao.RECEBIMENTO_TRANSFERENCIA)

                efetuarTrasferencia = contaOrigem.efetuarTrasferencia(efetuarTrasferencia!!)

                if (efetuarTrasferencia != null) {
                    receberTransferencia = contaDestino.recebimentoTransferencia(receberTransferencia!!)
                    if (receberTransferencia != null) {
                        serviceConta.update(contaOrigem)
                        serviceConta.update(contaDestino)
                        serviceOperacao.insert(efetuarTrasferencia)
                        serviceOperacao.insert(receberTransferencia)
                        return ResponseEntity.ok().body(OperacaoResponse(efetuarTrasferencia))
                    }
                }
            }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }


}
