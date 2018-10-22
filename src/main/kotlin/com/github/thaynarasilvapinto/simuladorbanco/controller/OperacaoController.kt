package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.controller.request.OperacaoRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.DepositoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.SaqueResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.TransferenciaResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping
open class OperacaoController {
    @Autowired
    private lateinit var serviceConta: ContaService
    @Autowired
    private lateinit var serviceOperacao: OperacaoService


    @PostMapping(value = "/conta/{id}/saque")
    fun saque(@PathVariable id: Int, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<SaqueResponse> {
        val conta = serviceConta.find(id)
        if (conta.isPresent) {
            var saque = Operacao(
                    contaOrigem = conta.get(),
                    contaDestino = conta.get(),
                    valorOperacao = operacao.valorOperacao!!,
                    tipoOperacao = Operacao.TipoOperacao.SAQUE)
            saque = conta.get().saque(operacao = saque)
            serviceConta.update(conta.get())
            saque = serviceOperacao.insert(saque)
            return ResponseEntity.ok().body(SaqueResponse(saque))
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/conta/{id}/deposito")
    fun deposito(@PathVariable id: Int, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<DepositoResponse> {
        val conta = serviceConta.find(id)
        if (conta.isPresent) {
            var deposito = Operacao(
                    contaOrigem = conta.get(),
                    contaDestino = conta.get(),
                    valorOperacao = operacao.valorOperacao!!,
                    tipoOperacao = Operacao.TipoOperacao.DEPOSITO)
            deposito = conta.get().deposito(deposito)
            serviceConta.update(conta.get())
            deposito = serviceOperacao.insert(deposito)
            return ResponseEntity.ok().body(DepositoResponse(deposito))
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/conta/{id}/transferencia")
    fun transferencia(@PathVariable id: Int, @Valid @RequestBody operacaoTransferenciaRequest: OperacaoRequest): ResponseEntity<TransferenciaResponse> {

        val contaOrigem = serviceConta.find(id)
        val contaDestino = serviceConta.find(id = operacaoTransferenciaRequest.contaDestino!!)


        if (id != operacaoTransferenciaRequest.contaDestino)
            if (contaDestino.isPresent && contaOrigem.isPresent) {

                var efetuarTrasferencia: Operacao? = Operacao(
                        contaDestino = contaDestino.get(),
                        contaOrigem = contaOrigem.get(),
                        valorOperacao = operacaoTransferenciaRequest.valorOperacao!!,
                        tipoOperacao = Operacao.TipoOperacao.TRANSFERENCIA)
                var receberTransferencia: Operacao? = Operacao(
                        contaDestino = contaDestino.get(),
                        contaOrigem = contaOrigem.get(), valorOperacao = operacaoTransferenciaRequest.valorOperacao,
                        tipoOperacao = Operacao.TipoOperacao.RECEBIMENTO_TRANSFERENCIA)

                efetuarTrasferencia = contaOrigem.get().efetuarTrasferencia(efetuarTrasferencia!!)
                receberTransferencia = contaDestino.get().recebimentoTransferencia(receberTransferencia!!)
                serviceConta.update(contaOrigem.get())
                serviceConta.update(contaDestino.get())
                efetuarTrasferencia = serviceOperacao.insert(efetuarTrasferencia)
                serviceOperacao.insert(receberTransferencia)
                return ResponseEntity.ok().body(TransferenciaResponse(efetuarTrasferencia))
            }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }


}
