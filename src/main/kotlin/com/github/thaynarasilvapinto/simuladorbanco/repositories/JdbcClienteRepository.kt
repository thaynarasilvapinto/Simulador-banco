package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.ClienteRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor.ClienteRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
open class JdbcClienteRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : ClienteRepository {

    companion object {
        const val TABLE_NAME = "cliente"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val CPF_COLUMN = "cpf"
        const val DATA_HORA_COLUMN = "data_hora"
        const val CONTA_ID_COLUMN = "conta_id"
    }

    override fun save(cliente: Cliente): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $NAME_COLUMN, $CPF_COLUMN, $DATA_HORA_COLUMN, $CONTA_ID_COLUMN)
                values (?,?,?,?,?::JSON, now())
                """
        return jdbcTemplate.update(
                sql,
                cliente.id,
                cliente.nome,
                cliente.cpf,
                cliente.dataHora
        )
    }

    override fun find(clienteId: Int): Cliente? {
        val sql = """
            select * from $TABLE_NAME where $$ID_COLUMN = ?
            """
        return try {
            jdbcTemplate.queryForObject(sql, ClienteRowMapper(), clienteId)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun update(cliente: Cliente): Int {
        val sql = """
            update $TABLE_NAME set $ID_COLUMN = ?, $NAME_COLUMN = ?, $CPF_COLUMN = ?, $DATA_HORA_COLUMN, $CONTA_ID_COLUMN = ?::JSON
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
                sql,
                cliente.nome,
                cliente.cpf,
                cliente.dataHora,
                cliente.conta
        )
    }

    override fun delete(id: Int): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }

    override fun findByCpfEquals(CPF: String): Cliente? {
        val sql = """
            select * from $TABLE_NAME where $$CPF_COLUMN = ?
            """
        return try {
            jdbcTemplate.queryForObject(sql, ClienteRowMapper())
        }catch (e: EmptyResultDataAccessException) {
            null
        }
    }
}