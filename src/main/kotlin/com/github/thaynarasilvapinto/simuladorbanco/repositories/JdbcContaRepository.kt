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


    companion object {
        const val TABLE_NAME = "conta"
        const val ID_COLUMN = "id"
        const val SALDO_COLUMN = "saldo"
        const val DATA_HORA_COLUMN = "data_hora"
        const val ID_ALIAS_COLUMN = "conta_id"
        const val ID_ALIAS_SALDO_ORIGEM_COLUMN = "origem_saldo"
        const val ID_ALIAS_DATA_ORIGEM_COLUMN = "origem_data_hora"
        const val ID_ALIAS_SALDO_DESTINO_COLUMN = "destino_saldo"
        const val ID_ALIAS_DATA_DESTINO_COLUMN = "destino_data_hora"
    }

    override fun save(conta: Conta): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $SALDO_COLUMN, $DATA_HORA_COLUMN)
                values (?,?,?)
                """

        return jdbcTemplate.update(
            sql,
            conta.id,
            conta.saldo,
            conta.dataHora
        )
    }

    override fun findById(contaId: String): Optional<Conta> {
        val sql = """
            select * from $TABLE_NAME where $ID_COLUMN = ?
            """
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ContaRowMapper(), contaId))
    }

    override fun update(conta: Conta): Int {
        val sql = """
            update $TABLE_NAME set $SALDO_COLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
            sql,
            conta.saldo,
            conta.id
        )
    }

    override fun deleteById(id: String): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }
}