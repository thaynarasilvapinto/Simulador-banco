package com.github.thaynarasilvapinto.simuladorbanco.domain.repository

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import java.util.*

interface ClienteRepository {
    fun save(cliente: Cliente): Int

    fun findById(clienteId: String): Optional<Cliente>

    fun update(cliente: Cliente): Int

    fun deleteById(id: String): Int

    fun findByCpfEquals(cpf: String): Optional<Cliente>
}