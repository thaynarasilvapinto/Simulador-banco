package com.github.thaynarasilvapinto.SimuladorBanco.repositories;

import com.github.thaynarasilvapinto.SimuladorBanco.domain.Conta;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.Operacao;
import com.github.thaynarasilvapinto.SimuladorBanco.domain.TipoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.List;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Integer> {

    List<Operacao> findAllByContaOrigem(Conta conta);
    List<Operacao> findAllByContaDestinoAndTipoOperacao(Conta conta, TipoOperacao operacao);
    List<Operacao> findAllByContaOrigemAndTipoOperacao(Conta conta, TipoOperacao operacao);

}