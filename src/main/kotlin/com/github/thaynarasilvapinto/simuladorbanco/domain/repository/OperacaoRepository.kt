package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import java.util.*

interface OperacaoRepository {
    fun save(operacao: Operacao): Int

    fun findById(operacaoId: String): Optional<Operacao>

    fun deleteById(id: String): Int

    fun findAllByContaOrigem(id: String): List<Operacao>

    fun findAllByContaDestinoAndTipoOperacao(id: String, operacao: String): List<Operacao>
}