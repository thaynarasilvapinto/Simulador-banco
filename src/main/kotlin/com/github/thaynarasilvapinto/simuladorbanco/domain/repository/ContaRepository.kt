package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta

interface ContaRepository {
    fun save(conta: Conta): Int

    fun find(contaId: Int): Conta?

    fun update(conta: Conta): Int

    fun delete(id: Int): Int
}