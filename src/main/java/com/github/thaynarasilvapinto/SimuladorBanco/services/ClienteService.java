package com.github.thaynarasilvapinto.SimuladorBanco.services;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import com.github.thaynarasilvapinto.SimuladorBanco.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repo;

    public Cliente find(Integer id){
        Optional<Cliente> obj = repo.findById(id);
        return obj.orElse(null);
    }
    public Cliente insert(Cliente obj){
        obj.setId(null);
        return repo.save(obj);
    }
    public Cliente update(Cliente obj) {
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
