package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import java.util.*

interface OperacaoRepository {
    fun save(operacao: Operacao): Int

    fun findById(operacaoId: Int): Optional<Operacao>

    fun update(operacao: Operacao): Int

    fun deleteById(id: Int): Int

    fun findAllByContaOrigem(conta: Conta): List<Operacao>

    fun findAllByContaDestinoAndTipoOperacao(conta: Conta, operacao: Operacao.TipoOperacao): List<Operacao>
}