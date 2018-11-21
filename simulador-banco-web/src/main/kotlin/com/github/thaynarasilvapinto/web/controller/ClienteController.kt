package com.github.thaynarasilvapinto.web.controller

import com.github.thaynarasilvapinto.api.ClienteApi
import com.github.thaynarasilvapinto.api.request.ClienteCriarRequest
import com.github.thaynarasilvapinto.api.response.ClienteResponse
import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.Conta
import com.github.thaynarasilvapinto.web.services.ClienteService
import com.github.thaynarasilvapinto.web.utils.toResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
open class ClienteController @Autowired constructor(
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