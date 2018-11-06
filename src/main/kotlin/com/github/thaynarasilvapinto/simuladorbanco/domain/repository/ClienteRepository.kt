package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import java.util.*

interface ClienteRepository {
    fun save(cliente: Cliente): Int

    fun findById(clienteId: Int): Optional<Cliente>

    fun update(cliente: Cliente): Int

    fun deleteById(id: Int): Int

    fun findByCpfEquals(CPF: String): Optional<Cliente>
}