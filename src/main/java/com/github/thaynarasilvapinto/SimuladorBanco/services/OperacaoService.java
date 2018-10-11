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
    public void deleteALL(){
        repo.deleteAll();
    }

    public List<Operacao> findAllContaOrigem(Conta conta){
        return repo.findAllByContaOrigem(conta);
    }
    public List<Operacao> findAllContaDestino(Conta conta){
        return repo.findAllByContaDestino(conta);
    }
    public List<Operacao> findAllTipoOperacao(TipoOperacao tipoOperacao){
        return repo.findAllByTipoOperacao(tipoOperacao);
    }

    public List<Operacao> extrato(Conta conta){
        List<Operacao> extrato = repo.findAllByContaOrigem(conta);
        List<Operacao> listaDeDestino = repo.findAllByContaDestino(conta);
        TipoOperacao operacao = null;

        for(int i=0; i<extrato.size(); i++){
            if(extrato.get(i).getTipoOperacao() == operacao.RECEBIMENTO_TRANSFERENCIA){
                extrato.remove(listaDeDestino.get(i));
            }
        }
        for(int i=0; i<listaDeDestino.size(); i++){
            if(listaDeDestino.get(i).getTipoOperacao() == operacao.RECEBIMENTO_TRANSFERENCIA){
                extrato.add(listaDeDestino.get(i));
            }
        }
        //Collections.sort(extrato);

        return extrato;
    }


}