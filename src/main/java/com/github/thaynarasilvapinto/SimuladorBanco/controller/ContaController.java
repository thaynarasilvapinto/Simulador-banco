package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {

    @Autowired
    private ContaService serviceConta = new ContaService();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Conta> find(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if(conta != null)
            return ResponseEntity.ok().body(conta);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @RequestMapping(value = "/{id}/saldo", method = RequestMethod.GET)
    public ResponseEntity<Double> saldo(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if(conta != null)
            return ResponseEntity.ok().body(conta.getSaldo());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
    @RequestMapping(value = "/{id}/extrato", method = RequestMethod.GET)
    public ResponseEntity<List<Operacao>> extrato(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if(conta != null)
            return ResponseEntity.ok().body(conta.getExtrato());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
}
