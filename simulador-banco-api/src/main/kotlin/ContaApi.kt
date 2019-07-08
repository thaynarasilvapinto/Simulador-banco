package com.github.thaynarasilvapinto.api

import com.github.thaynarasilvapinto.api.response.ContaResponse
import com.github.thaynarasilvapinto.api.response.ExtratoResponse
import com.github.thaynarasilvapinto.api.response.SaldoResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Api(tags = ["Conta"])
@RequestMapping(value = ["/conta"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface ContaApi {

    @ApiOperation(value = "Retorna uma conta", notes = "Utilize este método para buscar uma conta informando o id da mesma.")
    @GetMapping(value = ["/{id}"])
    fun find(@PathVariable id: String): ResponseEntity<ContaResponse>

    @ApiOperation(value = "Retorna o saldo de uma conta", notes = "Utilize este método para buscar o saldo de uma conta informando o id da mesma.")
    @GetMapping(value = ["/{id}/saldo"])
    fun saldo(@PathVariable id: String): ResponseEntity<SaldoResponse>

    @ApiOperation(value = "Retorna o extrato", notes = "Utilize este método para buscaro extrato de uma conta informando o id da mesma.")
    @GetMapping(value = ["/{id}/extrato"])
    fun extrato(@PathVariable id: String): ResponseEntity<List<ExtratoResponse>>?
}