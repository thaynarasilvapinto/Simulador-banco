package com.github.thaynarasilvapinto.SimuladorBanco.services;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.repositories.OperacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}