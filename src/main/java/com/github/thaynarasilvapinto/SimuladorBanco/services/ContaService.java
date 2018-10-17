package com.github.thaynarasilvapinto.SimuladorBanco.services;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository repo;

    public Conta find(Integer id){
        Optional<Conta> obj = repo.findById(id);
        return obj.orElse(null);
    }
    public Conta insert(Conta obj) {
        obj.setId(null);
        return repo.save(obj);
    }
    public Conta update(Conta obj) {
        find(obj.getId());
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
