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
@RequestMapping(value = "/")
open class ClienteController {

    @Autowired
    private lateinit var serviceCliente: ClienteService

    @Autowired
    private lateinit var serviceConta: ContaService

    @GetMapping("/cliente/{id}")
    fun mostrarCliente(@PathVariable id: Int): ResponseEntity<ClienteResponse> {
        val cliente = serviceCliente.find(id)
        return ResponseEntity.ok().body(ClienteResponse(ClienteResponse(cliente))) :? ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }

    @PostMapping(value = "/criar-cliente")
    fun criarCliente(@Valid @RequestBody clienteCriarRequest: ClienteCriarRequest): ResponseEntity<ClienteResponse> {

            var cliente = Cliente(nome = clienteCriarRequest.nome, clienteCriarRequest.cpf)
            cliente = serviceCliente.insert(cliente)
            val conta = Conta(0.00)
            val contaInserida = serviceConta!!.insert(conta)
            if (contaInserida != null) {
                cliente.conta = contaInserida
                serviceCliente.update(cliente)
                return ResponseEntity.ok().body(ClienteResponse(cliente))
        //return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null)
    }
}