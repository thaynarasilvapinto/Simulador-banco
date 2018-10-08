package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.ClienteResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.ValidaCPF;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/")
public class ClienteController {
    @Autowired
    private ClienteService serviceCliente;
    @Autowired
    private ContaService serviceConta;

    @RequestMapping(value = "/cliente/{id}", method = RequestMethod.GET)
    public ResponseEntity<ClienteResponse> mostrarCliente(@PathVariable Integer id) {
        Cliente cliente = serviceCliente.find(id);
        if (cliente != null) {
            return ResponseEntity.ok().body(new ClienteResponse(cliente));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @RequestMapping(value = "/criar-cliente", method = RequestMethod.POST)
    public ResponseEntity<ClienteResponse> criarCliente(@RequestBody Cliente c) {

        if(serviceCliente.findCPF(c.getCpf()) == null && new ValidaCPF().validaCPF(c.getCpf()) == true) {
            Cliente cliente = new Cliente(c.getNome(), c.getCpf(), null);
            Cliente clienteInserido = serviceCliente.insert(cliente);
            if (clienteInserido != null) {
                Conta conta = new Conta(0.00);
                Conta contaInserida = serviceConta.insert(conta);
                if (contaInserida != null) {
                    clienteInserido.setConta(contaInserida);
                    serviceCliente.update(clienteInserido);
                    return ResponseEntity.ok().body(new ClienteResponse(clienteInserido));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
}