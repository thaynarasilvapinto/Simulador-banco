package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.ContaResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.OperacaoResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {

    @Autowired
    private ContaService serviceConta = new ContaService();
    @Autowired
    private OperacaoService serviceOperacao = new OperacaoService();

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaResponse> find(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if (conta != null)
            return ResponseEntity.ok().body(new ContaResponse(conta));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @GetMapping(value = "/{id}/saldo")
    public ResponseEntity<String> saldo(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if (conta != null)
            return ResponseEntity.ok().body("Saldo: " + conta.getSaldo());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @GetMapping(value = "/{id}/extrato")
    public ResponseEntity<List<OperacaoResponse>> extrato(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);

        if (conta != null) {
            List<Operacao> lista = serviceOperacao.extrato(conta);
            List<OperacaoResponse> extrato = new ArrayList<>();

            for (int i = 0; i < lista.size(); i++) {
                extrato.add(new OperacaoResponse(lista.get(i)));
            }
            //TODO:Existe um jeito de fazer sem usar um for, usando map
            return ResponseEntity.ok().body(extrato);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
}
