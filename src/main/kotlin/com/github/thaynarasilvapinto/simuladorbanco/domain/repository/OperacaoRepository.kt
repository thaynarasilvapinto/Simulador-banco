package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import java.util.*

interface OperacaoRepository {
    fun save(operacao: Operacao): Int

    fun findById(operacaoId: Int): Optional<Operacao>

    fun deleteById(id: Int): Int

    fun findAllByContaOrigem(id: Int): List<Operacao>

    fun findAllByContaDestinoAndTipoOperacao(id: Int, operacao: String): List<Operacao>
}