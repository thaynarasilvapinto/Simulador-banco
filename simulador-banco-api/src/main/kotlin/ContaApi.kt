package com.github.thaynarasilvapinto.api

import com.github.thaynarasilvapinto.api.response.ContaResponse
import com.github.thaynarasilvapinto.api.response.ExtratoResponse
import com.github.thaynarasilvapinto.api.response.SaldoResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(value = ["/conta"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface ContaApi {

    @GetMapping(value = ["/{id}"])
    fun find(@PathVariable id: String): ResponseEntity<ContaResponse>

    @GetMapping(value = ["/{id}/saldo"])
    fun saldo(@PathVariable id: String): ResponseEntity<SaldoResponse>

    @GetMapping(value = ["/{id}/extrato"])
    fun extrato(@PathVariable id: String): ResponseEntity<List<ExtratoResponse>>?
}