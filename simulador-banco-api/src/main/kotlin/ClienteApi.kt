package com.github.thaynarasilvapinto.api

import com.github.thaynarasilvapinto.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.api.response.ClienteResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["Cliente"])
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
interface ClienteApi {

    @ApiOperation(value = "Retorna um cliente", notes = "Utilize este método para buscar um cliente informando o id do mesmo.")
    @GetMapping(value = ["/cliente/{id}"])
    fun mostrarCliente(@PathVariable("id") id: String): ResponseEntity<ClienteResponse>

    @ApiOperation(value = "Criar um cliente", notes = "Utilize este método para criar novos clientes.")
    @PostMapping(value = ["/criar-cliente"])
    fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse>
}