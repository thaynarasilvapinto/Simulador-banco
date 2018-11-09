package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Cliente
import java.util.*

interface ClienteRepository {
    fun save(cliente: Cliente): Int

    fun findById(clienteId: String): Optional<Cliente>

    fun update(cliente: Cliente): Int

    fun deleteById(id: String): Int

    fun findByCpfEquals(CPF: String): Optional<Cliente>
}