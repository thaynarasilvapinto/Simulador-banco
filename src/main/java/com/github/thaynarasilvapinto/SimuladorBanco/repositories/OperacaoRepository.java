package com.github.thaynarasilvapinto.SimuladorBanco.repositories;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Integer> {
}
