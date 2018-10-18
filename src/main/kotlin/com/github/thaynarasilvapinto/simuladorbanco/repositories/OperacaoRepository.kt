package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OperacaoRepository : JpaRepository<Operacao, Int> {

    fun findAllByContaOrigem(conta: Conta): List<Operacao>
    fun findAllByContaDestinoAndTipoOperacao(conta: Conta, operacao: TipoOperacao): List<Operacao>
    fun findAllByContaOrigemAndTipoOperacao(conta: Conta, operacao: TipoOperacao): List<Operacao>

}