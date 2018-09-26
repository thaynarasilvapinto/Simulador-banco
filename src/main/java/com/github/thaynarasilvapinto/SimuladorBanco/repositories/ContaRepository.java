package com.github.thaynarasilvapinto.SimuladorBanco.repositories;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {
}