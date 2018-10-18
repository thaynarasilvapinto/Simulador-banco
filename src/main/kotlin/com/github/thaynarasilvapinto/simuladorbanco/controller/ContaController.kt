package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ContaResponse
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
    fun find(@PathVariable id: Int?): ResponseEntity<ContaResponse> {
        val conta = serviceConta.find(id)
        return if (conta != null) ResponseEntity.ok().body(ContaResponse(conta)) else ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @GetMapping(value = "/{id}/saldo")
    fun saldo(@PathVariable id: Int?): ResponseEntity<String> {
        val conta = serviceConta.find(id)
        return if (conta != null) ResponseEntity.ok().body("Saldo: " + conta.saldo) else ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @GetMapping(value = "/{id}/extrato")
    fun extrato(@PathVariable id: Int?): ResponseEntity<List<Operacao>>? {
        emptyList<Operacao>()
        emptyList<OperacaoResponse>()

        val conta = serviceConta.find(id)

        if (conta != null) {
            val lista = serviceOperacao.extrato(conta)

/*
            var extrato: MutableList<OperacaoResponse>;

            for (i in lista.indices) {
                val add = sextrato.add(OperacaoResponse(lista[i]))
            }
*/

            return ResponseEntity.ok().body(lista)
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }
}
