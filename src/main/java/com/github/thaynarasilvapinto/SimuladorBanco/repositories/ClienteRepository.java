package com.github.thaynarasilvapinto.SimuladorBanco.repositories;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}