package com.github.thaynarasilvapinto.SimuladorBanco.resources;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/banco")
public class BancoResources {

    private Banco service = new Banco();

    @RequestMapping(value = "/cliente/{id}",method = RequestMethod.GET)
    public ResponseEntity<Cliente> mostrarCliente(@PathVariable Integer id){
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }
    @RequestMapping(value="/cliente/criar-cliente", method = RequestMethod.POST)
    public ResponseEntity<String> criarCliente(@RequestBody Cliente cliente){
        Cliente cliente1 = new Cliente(cliente.getNome(),cliente.getCpf());
        if(service.add(cliente1) == 1)
            return ResponseEntity.ok("Cliente inserido");
        return ResponseEntity.ok("Cliente n inserido");
    }

    @RequestMapping(value="/conta/{id}", method=RequestMethod.GET)
    public ResponseEntity<Conta> find(@PathVariable Integer id) {
        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(value="/conta/{id}/saldo",method = RequestMethod.GET)
    public ResponseEntity<Double> saldo(@PathVariable Integer id){

        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj.getSaldo());
    }
    @RequestMapping(value="/conta/{id}/extrato",method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Operacao>> extrato(@PathVariable Integer id){

        Conta obj = service.findConta(id);
        return ResponseEntity.ok().body(obj.getExtrato());
    }

    @RequestMapping(value="/conta/{id}/deposito",method = RequestMethod.POST)
    public ResponseEntity<String>  deposito(@PathVariable Integer id,@RequestBody Operacao operacao){
        Conta obj = service.findConta(id);
        Operacao deposito = new Operacao(id,id, operacao.getValorOperacao(), TipoOperacao.DEPOSITO);
        if(obj.deposito(deposito) == 1){
            return ResponseEntity.ok("A operacação realizada com sucesso");
        }
        return ResponseEntity.ok("A operacação não foi realizada");
    }

    @RequestMapping(value = "/conta/{id}/saque",method = RequestMethod.POST)
    public ResponseEntity<String> saque(@PathVariable Integer id,@RequestBody Operacao operacao){
        Operacao saque = new Operacao(id,id, operacao.getValorOperacao(), TipoOperacao.SAQUE);
        Conta obj = service.findConta(id);

        if(obj.saque(saque) == 1){
            return ResponseEntity.ok("A operacação realizada com sucesso");
        }
        return ResponseEntity.ok("A operacação não foi realizada");
    }

    @RequestMapping(value = "/conta/{id}/transferencia",method = RequestMethod.POST)
    public  ResponseEntity<String> transaferencia(@PathVariable Integer id,@RequestBody Operacao operacao){

        Operacao transferencia = new Operacao(id,
                operacao.getIdDestino(), operacao.getValorOperacao(),
                TipoOperacao.TRANSFERENCIA);
        Conta obj = service.findConta(id);
        if(obj.Transferencia(service.findConta(operacao.getIdDestino()),transferencia) == 1){
            return ResponseEntity.ok("A operacação realizada com sucesso");
        }
        return ResponseEntity.ok("A operacação não foi realizada");
    }
}