package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ContaResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ExtratoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.SaldoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.SaqueResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import com.github.thaynarasilvapinto.simuladorbanco.services.OperacaoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/conta")
open class ContaController {

    @Autowired
    private lateinit var serviceConta: ContaService
    @Autowired
    private lateinit var serviceOperacao: OperacaoService

    @GetMapping(value = "/{id}")
    fun find(@PathVariable id: Int): ResponseEntity<ContaResponse> {
        val conta = this.serviceConta.find(id)
        if (conta.isPresent) return ResponseEntity.ok().body(ContaResponse(conta.get()))
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @GetMapping(value = "/{id}/saldo")
    fun saldo(@PathVariable id: Int): ResponseEntity<SaldoResponse> {
        val conta = serviceConta.find(id)
        if (conta.isPresent) return ResponseEntity.ok().body(SaldoResponse(conta.get()))
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @GetMapping(value = "/{id}/extrato")
    fun extrato(@PathVariable id: Int): ResponseEntity<List<ExtratoResponse>>? {

        val conta = serviceConta.find(id)

        if (conta.isPresent) {

            val lista = serviceOperacao.extrato(conta.get())

            val extrato = lista.map {ExtratoResponse(
                    idOperacao = it.idOperacao,
                    valorOperacao = it.valorOperacao,
                    dataHora = it.dataHoraOperacao,
                    tipoOperacao = it.tipoOperacao,
                    contaDestino = it.contaDestino.id)}

            return ResponseEntity.ok().body(extrato)
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }
}
