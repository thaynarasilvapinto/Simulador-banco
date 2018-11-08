package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import java.util.*

interface ContaRepository {
    fun save(conta: Conta): Int

    fun findById(contaId: String): Optional<Conta>

    fun update(conta: Conta): Int

    fun deleteById(id: String): Int
}