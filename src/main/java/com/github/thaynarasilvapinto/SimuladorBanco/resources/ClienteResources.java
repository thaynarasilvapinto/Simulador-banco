package com.github.thaynarasilvapinto.SimuladorBanco.resources;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/clientes")
public class ClienteResources{

    @Autowired
    private ClienteService service;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }
    @RequestMapping(value="/criar-cliente", method = RequestMethod.POST)
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente){
        return ResponseEntity.ok("Cliente inserido");
    }
}
