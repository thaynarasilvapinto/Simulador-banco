package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Conta
import java.util.*

interface ContaRepository {
    fun save(conta: Conta): Int

    fun findById(contaId: String): Conta?

    fun update(conta: Conta): Int

    fun deleteById(id: String): Int
}