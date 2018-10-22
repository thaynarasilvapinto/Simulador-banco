package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClienteRepository : JpaRepository<Cliente, Int> {
    fun findByCpfEquals(CPF: String): Optional<Cliente>
}