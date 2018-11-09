package com.github.thaynarasilvapinto.repositories

import com.github.thaynarasilvapinto.model.Cliente
import com.github.thaynarasilvapinto.model.repository.ClienteRepository
import com.github.thaynarasilvapinto.repositories.extractor.ClienteRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class JdbcClienteRepository @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate
) : ClienteRepository {


    companion object {
        const val TABLE_NAME = "cliente"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "nome"
        const val CPF_COLUMN = "cpf"
        const val DATA_HORA_COLUMN = "data_hora"
        const val CONTA_ID_COLUMN = "conta_id"
    }

    override fun save(cliente: Cliente): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $NAME_COLUMN, $CPF_COLUMN, $DATA_HORA_COLUMN, $CONTA_ID_COLUMN)
                values (?, ?, ?, ?, ?)
                """

        return jdbcTemplate.update(
            sql,
            cliente.id,
            cliente.nome,
            cliente.cpf,
            cliente.dataHora,
            cliente.conta.id
        )
    }

    override fun findById(clienteId: String): Optional<Cliente> {
        val sql = """
                SELECT
                       cliente.*,
                       conta.data_hora,
                       conta.saldo
                FROM
                     cliente,
                     conta
                WHERE
                    cliente.conta_id = conta.id AND
                    cliente.id = ?
                """

        return try {
            Optional.ofNullable(jdbcTemplate.queryForObject(sql, ClienteRowMapper(), clienteId))
        } catch (e: EmptyResultDataAccessException) {
            Optional.ofNullable(null)
        }
    }

    override fun update(cliente: Cliente): Int {
        val sql = """
            update $TABLE_NAME set
                   $NAME_COLUMN = ?,
                   $CPF_COLUMN = ?,
                   $CONTA_ID_COLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
            sql,
            cliente.nome,
            cliente.cpf,
            cliente.conta.id,
            cliente.id
        )
    }

    override fun deleteById(id: String): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }

    override fun findByCpfEquals(cpf: String): Optional<Cliente> {
        val sql = """
                SELECT
                       cliente.*,
                       conta.data_hora,
                       conta.saldo
                FROM
                     cliente,
                     conta
                WHERE
                    cliente.conta_id = conta.id AND
                    cliente.cpf = ?
            """

        return try {
            Optional.ofNullable(jdbcTemplate.queryForObject(sql, ClienteRowMapper(), cpf))
        } catch (e: EmptyResultDataAccessException) {
            Optional.ofNullable(null)
        }
    }
}