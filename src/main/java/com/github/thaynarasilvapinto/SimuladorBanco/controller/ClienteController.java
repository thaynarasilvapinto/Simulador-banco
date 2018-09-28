package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteController {

    private ClienteService service = new ClienteService();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> mostrarCliente(@PathVariable Integer id) {
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(value = "/criar-cliente", method = RequestMethod.POST)
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {

        Cliente cliente1 = new Cliente(cliente.getNome(), cliente.getCpf());
        service.insert(cliente1);
        //if (service.insert(cliente1) != null)
            //return ResponseEntity.ok().body(cliente1);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

}
