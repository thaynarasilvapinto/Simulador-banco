package com.github.thaynarasilvapinto.SimuladorBanco.services;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import com.github.thaynarasilvapinto.SimuladorBanco.repositories.OperacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OperacaoService {

    @Autowired
    private OperacaoRepository repo;

    public Operacao find(Integer id){
        Optional<Operacao> obj = repo.findById(id);
        return obj.orElse(null);
    }
    public Operacao insert(Operacao obj) {
        obj.setIdOperacao(null);
        return repo.save(obj);
    }
    public Operacao update(Operacao obj) {
        find(obj.getIdOperacao());
        return repo.save(obj);
    }
    public void delete(Integer id) {
        find(id);
        repo.deleteById(id);
    }

    public List<Operacao> findAllContaOrigem(Conta conta){
        return repo.findAllByContaOrigem(conta);
    }
    public List<Operacao> extrato(Conta conta){

        TipoOperacao tipoOperacao = null;

        List<Operacao> extrato = repo.findAllByContaDestinoAndTipoOperacao(conta, tipoOperacao.RECEBIMENTO_TRANSFERENCIA);
        List<Operacao> trasferencia = repo.findAllByContaOrigemAndTipoOperacao(conta, tipoOperacao.TRANSFERENCIA);
        List<Operacao> deposito = repo.findAllByContaOrigemAndTipoOperacao(conta, tipoOperacao.DEPOSITO);
        List<Operacao> saque = repo.findAllByContaOrigemAndTipoOperacao(conta, tipoOperacao.SAQUE);

        for(int i=0; i<trasferencia.size();i++){
            extrato.add(trasferencia.get(i));
        }
        for(int i=0; i<deposito.size();i++){
            extrato.add(deposito.get(i));
        }
        for(int i=0; i<saque.size();i++){
            extrato.add(saque.get(i));
        }
        //TODO:Ordenar a lista
        return extrato;
    }


}