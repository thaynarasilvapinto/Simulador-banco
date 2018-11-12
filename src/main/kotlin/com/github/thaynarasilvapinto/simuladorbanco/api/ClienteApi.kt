package com.github.thaynarasilvapinto.simuladorbanco.api

import com.github.thaynarasilvapinto.simuladorbanco.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ClienteResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
interface ClienteApi {

    @GetMapping(value = ["/cliente/{id}"])
    fun mostrarCliente(@PathVariable("id") id: String): ResponseEntity<ClienteResponse>

    @PostMapping(value = ["/criar-cliente"])
    fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse>
}