package com.github.thaynarasilvapinto.SimuladorBanco.resources;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Banco;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/conta")
public class ContaResources{
    
    private Banco service;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<Conta> find(@PathVariable Integer id) {
        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(value="/{id}/saldo",method = RequestMethod.GET)
    public ResponseEntity<Double> saldo(@PathVariable Integer id){

        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj.getSaldo());
    }
    @RequestMapping(value="/{id}/extrato",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Operacao>> extrato(@PathVariable Integer id){

        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj.getExtrato());
    }

    @RequestMapping(value="/{id}/deposito",method = RequestMethod.POST)
    public ResponseEntity<String>  deposito(@PathVariable Integer id,@RequestBody Operacao operacao){
        Conta obj = service.findConta(id);
        Operacao deposito = new Operacao(id,operacao.getIdDestino(), operacao.getValorOperacao(), TipoOperacao.DEPOSITO);
        if(obj.deposito(deposito) == 1){
            //service.update(obj);
            return ResponseEntity.ok("A opercação realizada com sucesso");
        }
        return ResponseEntity.ok("A opercação não foi realizada, verifique seu saldo");
    }

    @RequestMapping(value = "/{id}/saque",method = RequestMethod.POST)
    public ResponseEntity<String> saque(@PathVariable Integer id,@RequestBody Operacao operacao){
        Operacao saque = new Operacao(id,id, operacao.getValorOperacao(), TipoOperacao.SAQUE);
        Conta obj = service.findConta(id);

        if(obj.saque(saque) == 1){
            //service.update(obj);
            return ResponseEntity.ok("A opercação realizada com sucesso");
        }
        return ResponseEntity.ok("A opercação não foi realizada, verifique seu saldo");
    }

    @RequestMapping(value = "{id}/transferencia",method = RequestMethod.POST)
    public  ResponseEntity<String> transaferencia(@PathVariable Integer id,@RequestBody Operacao operacao){

        Operacao transferencia = new Operacao(operacao.getIdOrigem(),
                operacao.getIdDestino(),
                operacao.getValorOperacao(),
                TipoOperacao.TRANSFERENCIA);
        Conta obj = service.findConta(id);
        if(obj.Transferencia(service.findConta(transferencia.getIdDestino()),transferencia) == 1){
            //service.update(obj);
            return ResponseEntity.ok("A opercação realizada com sucesso");
        }
        return ResponseEntity.ok("A opercação não foi realizada, verifique seu saldo");
    }
}