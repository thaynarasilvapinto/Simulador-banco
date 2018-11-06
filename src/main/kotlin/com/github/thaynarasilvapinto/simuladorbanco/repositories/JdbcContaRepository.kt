package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor.ContaRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class JdbcContaRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : ContaRepository {

    var id = 0

    companion object {
        const val TABLE_NAME = "conta"
        const val ID_COLUMN = "id"
        const val SALDO_COLUMN = "saldo"
        const val DATA_HORA_COLUMN = "data_hora"
    }

    override fun save(conta: Conta): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $SALDO_COLUMN, $DATA_HORA_COLUMN)
                values (id,?,?)
                """

        id++
        return jdbcTemplate.update(
                sql,
                conta.id,
                conta.saldo,
                conta.dataHora
        )
    }

    override fun findById(contaId: Int): Optional<Conta> {
        val sql = """
            select * from $TABLE_NAME where $ID_COLUMN = ?
            """
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ContaRowMapper(), contaId))
    }

    override fun update(conta: Conta): Int {
        val sql = """
            update $TABLE_NAME set $ID_COLUMN = ?, $SALDO_COLUMN = ?, $DATA_HORA_COLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
                sql,
                conta.id,
                conta.saldo,
                conta.dataHora
        )
    }

    override fun deleteById(id: Int): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }
}