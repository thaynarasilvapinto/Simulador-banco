package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente

interface ClienteRepository {
    fun save(cliente: Cliente): Int

    fun find(clienteId: Int): Cliente?

    fun update(cliente: Cliente): Int

    fun delete(id: Int): Int

    fun findByCpfEquals(CPF: String): Cliente?
}