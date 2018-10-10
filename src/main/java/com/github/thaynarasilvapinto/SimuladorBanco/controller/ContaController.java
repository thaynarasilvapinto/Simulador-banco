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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {

    @Autowired
    private ContaService serviceConta = new ContaService();
    @Autowired
    private OperacaoService serviceOperacao = new OperacaoService();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ContaResponse> find(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if (conta != null)
            return ResponseEntity.ok().body(new ContaResponse(conta));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @RequestMapping(value = "/{id}/saldo", method = RequestMethod.GET)
    public ResponseEntity<String> saldo(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);
        if (conta != null)
            return ResponseEntity.ok().body("Saldo: " + conta.getSaldo());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @RequestMapping(value = "/{id}/extrato", method = RequestMethod.GET)
    public ResponseEntity<List<OperacaoResponse>> extrato(@PathVariable Integer id) {
        Conta conta = serviceConta.find(id);

        if (conta != null) {
            List<Operacao> listaOperacao = serviceOperacao.findAllContaOrigem(conta);
            List<OperacaoResponse> extrato = new ArrayList<>();
            for (int i = 0; i < listaOperacao.size(); i++) {
                extrato.add(new OperacaoResponse(listaOperacao.get(i)));
            }
            //TODO:Existe um jeito de fazer sem usar um for, usando map
            return ResponseEntity.ok().body(extrato);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
}
