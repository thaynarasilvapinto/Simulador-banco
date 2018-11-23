package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Operacao
import java.util.*

interface OperacaoRepository {
    fun save(operacao: Operacao): Int

    fun findById(operacaoId: String): Operacao?

    fun deleteById(id: String): Int

    fun findAllByContaOrigem(id: String): List<Operacao>

    fun findAllByContaDestinoAndTipoOperacao(id: String, operacao: String): List<Operacao>

    fun findAllByContaOrigemAndTipoOperacao(id: String, operacao: String): List<Operacao>
}