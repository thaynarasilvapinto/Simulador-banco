package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.api.ContaApi
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ContaResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ExtratoResponse
import com.github.thaynarasilvapinto.simuladorbanco.api.response.SaldoResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponseSaldo
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
open class ContaController(
    private val serviceConta: ContaService
) : ContaApi {

    override fun find(@PathVariable id: String): ResponseEntity<ContaResponse> {
        val conta = serviceConta.conta(id)
        return ResponseEntity.ok().body(conta.toResponse())
    }

    override fun saldo(@PathVariable id: String): ResponseEntity<SaldoResponse> {
        val saldo = serviceConta.saldo(id)
        return ResponseEntity.ok().body(saldo.toResponseSaldo())
    }

    override fun extrato(@PathVariable id: String): ResponseEntity<List<ExtratoResponse>>? {
        val lista = serviceConta.extrato(id)
        val extrato = lista.map {
            ExtratoResponse(
                idOperacao = it.idOperacao,
                valorOperacao = it.valorOperacao,
                dataHora = it.dataHoraOperacao,
                tipoOperacao = enumValueOf(it.tipoOperacao.name),
                contaDestino = it.contaDestino.id
            )
        }
        return ResponseEntity.ok().body(extrato)
    }
}