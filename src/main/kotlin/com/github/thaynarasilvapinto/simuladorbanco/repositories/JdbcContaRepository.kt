package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ContaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
open class JdbcContaRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : ContaRepository {
    companion object {
        const val TABLE_NAME = "conta"
        const val ID_COLUMN = "id"
        const val SALDO_COLUMN = "saldo"
        const val DATA_HORA_COLUMN = "data_hora"
    }

    override fun save(conta: Conta): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(contaId: Int): Conta? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(conta: Conta): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}