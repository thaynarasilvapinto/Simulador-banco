package com.github.thaynarasilvapinto.SimuladorBanco.controller;

import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoDepositoRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoSaqueRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.request.OperacaoTransferenciaRequest;
import com.github.thaynarasilvapinto.SimuladorBanco.controller.response.OperacaoResponse;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.services.ContaService;
import com.github.thaynarasilvapinto.SimuladorBanco.services.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/")
public class OperacaoController {
    @Autowired
    private ContaService serviceConta = new ContaService();
    @Autowired
    private OperacaoService serviceOperacao = new OperacaoService();


    @GetMapping(value = "/operacao/{id}")
    public ResponseEntity<OperacaoResponse> find(@PathVariable Integer id) {
        Operacao operacao = serviceOperacao.find(id);
        if (operacao != null)
            return ResponseEntity.ok().body(new OperacaoResponse(operacao));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @PostMapping(value = "/conta/{id}/saque")
    public ResponseEntity<OperacaoResponse> saque(@PathVariable Integer id, @Valid @RequestBody OperacaoSaqueRequest operacao) {
        Conta conta = serviceConta.find(id);

        if (conta != null) {
            if (operacao.getValorOperacao() > 0) {
                Operacao saque = new Operacao(conta, conta, operacao.getValorOperacao(), TipoOperacao.SAQUE);
                saque = serviceOperacao.insert(saque);
                if (saque != null) {
                    saque = conta.saque(saque);
                    if (saque != null) {
                        serviceConta.update(conta);
                        return ResponseEntity.ok().body(new OperacaoResponse(saque));
                    } else {
                        serviceOperacao.delete(saque.getIdOperacao());
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @PostMapping(value = "/conta/{id}/deposito")
    public ResponseEntity<OperacaoResponse> deposito(@PathVariable Integer id, @Valid @RequestBody OperacaoDepositoRequest operacao) {
        Conta conta = serviceConta.find(id);
        if (conta != null) {
            if (operacao.getValorOperacao() > 0) {
                Operacao deposito = new Operacao(conta, conta, operacao.getValorOperacao(), TipoOperacao.DEPOSITO);
                deposito = serviceOperacao.insert(deposito);
                if (deposito != null) {
                    deposito = conta.deposito(deposito);
                    if (deposito != null) {
                        serviceConta.update(conta);
                        return ResponseEntity.ok().body(new OperacaoResponse(deposito));
                    } else {
                        serviceOperacao.delete(deposito.getIdOperacao());
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }

    @PostMapping(value = "/conta/{id}/transferencia")
    public ResponseEntity<OperacaoResponse> transferencia(@PathVariable Integer id, @Valid @RequestBody OperacaoTransferenciaRequest operacaoTransferenciaRequest) {

        //TODO: TENTAR tirar um pouco desses IFs

        Conta contaOrigem = serviceConta.find(id);
        Conta contaDestino = serviceConta.find(operacaoTransferenciaRequest.getContaDestino());

        if (id != operacaoTransferenciaRequest.getContaDestino())
            if (contaDestino != null && contaOrigem != null) {
                Operacao efetuarTrasferencia
                        = new Operacao(contaDestino, contaOrigem, operacaoTransferenciaRequest.getValorOperacao(), TipoOperacao.TRANSFERENCIA);
                Operacao receberTransferencia
                        = new Operacao(contaDestino, contaOrigem, operacaoTransferenciaRequest.getValorOperacao(), TipoOperacao.RECEBIMENTO_TRANSFERENCIA);

                efetuarTrasferencia = serviceOperacao.insert(efetuarTrasferencia);
                if (efetuarTrasferencia != null) {
                    receberTransferencia = serviceOperacao.insert(receberTransferencia);
                    if (receberTransferencia != null) {
                        efetuarTrasferencia = contaOrigem.efetuarTrasferencia(efetuarTrasferencia);
                        if (efetuarTrasferencia != null) {
                            receberTransferencia = contaDestino.recebimentoTransferencia(receberTransferencia);
                            if (receberTransferencia != null) {
                                serviceConta.update(contaOrigem);
                                serviceConta.update(contaDestino);
                                return ResponseEntity.ok().body(new OperacaoResponse(efetuarTrasferencia));
                            } else {
                                serviceOperacao.delete(receberTransferencia.getIdOperacao());
                                serviceOperacao.delete(efetuarTrasferencia.getIdOperacao());
                            }
                        } else {
                            serviceOperacao.delete(receberTransferencia.getIdOperacao());
                            serviceOperacao.delete(efetuarTrasferencia.getIdOperacao());
                        }

                    } else {
                        serviceOperacao.delete(efetuarTrasferencia.getIdOperacao());
                    }
                }
            }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
    }


}
