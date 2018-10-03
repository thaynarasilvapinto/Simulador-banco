package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class ContaController {
    @Autowired
    private ContaService serviceConta = new ContaService();
    @Autowired
    private OperacaoService serviceOperacao = new OperacaoService();
    @RequestMapping(value = "/conta/{id}", method = RequestMethod.GET)
    public ResponseEntity<Conta> find(@PathVariable Integer id) {
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        Conta conta = serviceConta.find(id);
        return ResponseEntity.ok().body(conta);
    }

    @RequestMapping(value = "/conta/{id}/saldo", method = RequestMethod.GET)
    public ResponseEntity<Double> saldo(@PathVariable Integer id) {
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        Conta conta = serviceConta.find(id);
        return ResponseEntity.ok().body(conta.getSaldo());
    }
    @RequestMapping(value = "/conta/{id}/extrato", method = RequestMethod.GET)
    public ResponseEntity<List<Operacao>> extrato(@PathVariable Integer id) {
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        Conta obj = serviceConta.find(id);
        return ResponseEntity.ok().body(obj.getExtrato());
    }
    @RequestMapping(value = "/conta/{id}/saque", method = RequestMethod.POST)
    public ResponseEntity<Operacao> saque(@PathVariable Integer id, @RequestBody Operacao operacao) {
        Conta conta = serviceConta.find(id);
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        Operacao saque = new Operacao(conta, conta, operacao.getValorOperacao(), TipoOperacao.SAQUE);
        serviceOperacao.insert(saque);

        saque = conta.saque(saque);

        if (saque != null){
            serviceConta.update(conta);
            serviceOperacao.update(operacao);
            return ResponseEntity.ok().body(saque);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
    @RequestMapping(value = "/conta/{id}/deposito", method = RequestMethod.POST)
    public ResponseEntity<Operacao> deposito(@PathVariable Integer id, @RequestBody Operacao operacao) {
        Conta conta = serviceConta.find(id);
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        Operacao deposito = new Operacao(conta, conta, operacao.getValorOperacao(), TipoOperacao.DEPOSITO);
        serviceOperacao.insert(deposito);

        deposito= conta.deposito(deposito);

        if (deposito != null) {
            serviceConta.update(conta);
            serviceOperacao.update(operacao);
            return ResponseEntity.ok().body(deposito);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
    @RequestMapping(value = "/conta/{id}/transferencia", method = RequestMethod.POST)
    public ResponseEntity<Operacao> transaferencia(@PathVariable Integer id, @RequestBody Operacao operacao) {
        Conta contaOrigem = serviceConta.find(id);
        Conta contaDestino = serviceConta.find(operacao.getContaDestino().getId());

        Operacao efetuarTrasferencia = new Operacao(contaOrigem,contaDestino,operacao.getValorOperacao(),TipoOperacao.TRANSFERENCIA);
        Operacao receberTransferencia = new Operacao(contaOrigem,contaDestino,operacao.getValorOperacao(),TipoOperacao.RECEBIMENTO_TRANSFERENCIA);
        serviceOperacao.insert(efetuarTrasferencia);
        serviceOperacao.insert(receberTransferencia);
        //TODO:Precisa-se fazer uma operção para validar se os clientes estão no banco
        if(efetuarTrasferencia.getValorOperacao() <= contaOrigem.getSaldo()){
            efetuarTrasferencia = contaOrigem.efetuarTrasferencia(efetuarTrasferencia);
            receberTransferencia = contaDestino.recebimentoTransferencia(receberTransferencia);

            if(efetuarTrasferencia != null && receberTransferencia != null){
                serviceConta.update(contaOrigem);
                serviceConta.update(contaDestino);
                return ResponseEntity.ok().body(efetuarTrasferencia);
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }
}
