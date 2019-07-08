package com.github.thaynarasilvapinto.api

import com.github.thaynarasilvapinto.api.request.OperacaoRequest
import com.github.thaynarasilvapinto.api.response.DepositoResponse
import com.github.thaynarasilvapinto.api.response.SaqueResponse
import com.github.thaynarasilvapinto.api.response.TransferenciaResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Api(tags = ["Operacao"])
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
interface OperacaoApi {

    @ApiOperation(value = "Realizar saque", notes = "Utilize este método para realizar um saque.")
    @PostMapping(value = ["/conta/{id}/saque"])
    fun saque(@PathVariable id: String, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<SaqueResponse>

    @ApiOperation(value = "Realizar deposito", notes = "Utilize este método para realizar um deposito.")
    @PostMapping(value = ["/conta/{id}/deposito"])
    fun deposito(@PathVariable id: String, @Valid @RequestBody operacao: OperacaoRequest): ResponseEntity<DepositoResponse>

    @ApiOperation(value = "Realizar transferência", notes = "Utilize este método para realizar uma transferência.")
    @PostMapping(value = ["/conta/{id}/transferencia"])
    fun transferencia(@PathVariable id: String, @Valid @RequestBody operacaoTransferenciaRequest: OperacaoRequest): ResponseEntity<TransferenciaResponse>
}