package com.github.thaynarasilvapinto.simuladorbanco.controller

import com.github.thaynarasilvapinto.simuladorbanco.controller.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.controller.response.ClienteResponse
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

    @Autowired
    private lateinit var serviceConta: ContaService

    @GetMapping(value = ["/cliente/{id}"])
    fun mostrarCliente(@PathVariable id: Int): ResponseEntity<ClienteResponse> {

        val cliente = this.serviceCliente.find(id)

        if (cliente.isPresent) {
            return ResponseEntity.ok().body(ClienteResponse(cliente.get()))
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = ["/criar-cliente"])
    fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse> {
        if (this.serviceCliente.findCPF(clienteCriarRequest.cpf!!).isPresent) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
        } else {
            val conta = serviceConta.insert(Conta(saldo = 2.00))
            val cliente = Cliente(nome = clienteCriarRequest.nome!!, cpf = clienteCriarRequest.cpf!!, conta = conta)
            val clienteInserido = serviceCliente.insert(cliente)
            return ResponseEntity.ok().body(ClienteResponse(clienteInserido))
        }

    }
}