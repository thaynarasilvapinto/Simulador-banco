package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Transaction

interface TransactionRepository {

    fun save(transaction: Transaction): Int

    fun findById(id: String): Transaction?

    fun update(transaction: Transaction): Int

    fun deleteById(id: String): Int

    fun findAllByaccountOriginId(id: String): List<Transaction>

    fun findAllByContaDestinoAndTipoOperacao(id: String, operacao: String): List<Transaction>

    fun findAllByContaOrigemAndTipoOperacao(id: String, operacao: String): List<Transaction>
}