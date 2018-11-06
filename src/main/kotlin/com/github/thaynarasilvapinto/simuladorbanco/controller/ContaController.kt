package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.api.response.ContaResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ExtratoResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.SaldoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseSaldo
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/conta"])
open class ContaController {

    @Autowired
    private lateinit var serviceConta: ContaService

    @GetMapping(value = ["/{id}"])
    fun find(@PathVariable id: Int): ResponseEntity<ContaResponse> {
        val conta = serviceConta.conta(id)
        return ResponseEntity.ok().body(conta.toResponse())
    }

    @GetMapping(value = ["/{id}/saldo"])
    fun saldo(@PathVariable id: Int): ResponseEntity<SaldoResponse> {
        val saldo = serviceConta.saldo(id)
        return ResponseEntity.ok().body(saldo.toResponseSaldo())
    }

    @GetMapping(value = ["/{id}/extrato"])
    fun extrato(@PathVariable id: Int): ResponseEntity<List<ExtratoResponse>>? {
        val lista = serviceConta.extrato(id)
        val extrato = lista.map {
            ExtratoResponse(
                    idOperacao = it.idOperacao,
                    valorOperacao = it.valorOperacao,
                    dataHora = it.dataHoraOperacao,
                    tipoOperacao = enumValueOf(it.tipoOperacao.name),
                    contaDestino = it.contaDestino)
        }
        return ResponseEntity.ok().body(extrato)
    }
}