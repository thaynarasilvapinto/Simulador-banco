package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ClienteResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import com.github.thaynarasilvapinto.simuladorbanco.services.ContaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/"])
open class ClienteController {
    @Autowired
    private lateinit var serviceCliente: ClienteService


    @GetMapping(value = ["/cliente/{id}"])
    fun mostrarCliente(@PathVariable id: Int): ResponseEntity<ClienteResponse> {
        val cliente = serviceCliente.cliente(id)
        return ResponseEntity.ok().body(cliente.toResponse())

    }

    @PostMapping(value = ["/criar-cliente"])
    fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse> {
        val cliente = serviceCliente.criarCliente(Cliente(nome = clienteCriarRequest.nome!!, cpf = clienteCriarRequest.cpf!!, conta = -1))
        return ResponseEntity.ok().body(cliente.toResponse())

    }
}