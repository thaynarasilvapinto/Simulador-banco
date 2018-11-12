package com.github.thaynarasilvapinto.simuladorbanco.controller


import com.github.thaynarasilvapinto.simuladorbanco.api.ClienteApi
import com.github.thaynarasilvapinto.simuladorbanco.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.simuladorbanco.api.response.ClienteResponse
import com.github.thaynarasilvapinto.simuladorbanco.controller.utils.toResponse
import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.services.ClienteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
open class ClienteController(
    private val serviceCliente: ClienteService
) : ClienteApi {

    override fun mostrarCliente(@PathVariable("id") id: String): ResponseEntity<ClienteResponse> {
        val cliente = serviceCliente.cliente(id)
        return ResponseEntity.ok().body(cliente.toResponse())

    }

    override fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse> {
        val cliente = serviceCliente.criarCliente(
            Cliente(
                nome = clienteCriarRequest.nome!!,
                cpf = clienteCriarRequest.cpf!!,
                conta = Conta(saldo = 0.00)
            )
        )
        return ResponseEntity.ok().body(cliente.toResponse())

    }
}