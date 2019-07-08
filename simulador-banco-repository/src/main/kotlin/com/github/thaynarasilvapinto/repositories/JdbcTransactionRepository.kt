package com.github.thaynarasilvapinto.repositories

import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.model.repository.TransactionRepository
import com.github.thaynarasilvapinto.repositories.extractor.OperacaoRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
open class JdbcTransactionRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : TransactionRepository {


    companion object {
        const val TABLE_NAME = "operacao"
        const val ID_COLUMN = "id_operacao"
        const val VALOR_OPERACAO_COLUMN = "valor_operacao"
        const val DATA_HORA_OPERACAO_COLUMN = "data_hora_operacao"
        const val TIPO_OPERACAO_COLUMN = "tipo_operacao"
        const val CONTA_ORIGEM_CONLUMN = "conta_id_origem"
        const val CONTA_DESTINO_CONLUMN = "conta_id_destino"
    }

    override fun save(transaction: Transaction): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $VALOR_OPERACAO_COLUMN, $DATA_HORA_OPERACAO_COLUMN, $TIPO_OPERACAO_COLUMN, $CONTA_ORIGEM_CONLUMN, $CONTA_DESTINO_CONLUMN)
                values (?,?,?,?,?,?)
                """

        return jdbcTemplate.update(
            sql,
            transaction.idOperacao,
            transaction.valorOperacao,
            transaction.dataHoraOperacao,
            transaction.tipoOperacao.name,
            transaction.accountOrigem.id,
            transaction.accountDestino.id
        )
    }

    override fun findById(operacaoId: String): Transaction? {
        val sql = """
            SELECT
                operacao.*,
                origem.data_hora AS origem_data_hora,
                origem.saldo AS origem_saldo,
                   destino.data_hora AS destino_data_hora,
                   destino.saldo AS destino_saldo
            FROM
                 operacao
                 INNER JOIN conta origem ON operacao.conta_id_origem = origem.id
                 INNER JOIN conta destino ON operacao.conta_id_destino = destino.id
            WHERE
                operacao.id_operacao = ?
            """

        return try {
            jdbcTemplate.queryForObject(sql, OperacaoRowMapper(), operacaoId)
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    }


    override fun deleteById(id: String): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }

    override fun findAllByContaOrigem(id: String): List<Transaction> {
        val sql = """
            SELECT
                operacao.*,
                origem.data_hora AS origem_data_hora,
                origem.saldo AS origem_saldo,
                   destino.data_hora AS destino_data_hora,
                   destino.saldo AS destino_saldo
            FROM
                 operacao
                 INNER JOIN conta origem ON operacao.conta_id_origem = origem.id
                 INNER JOIN conta destino ON operacao.conta_id_destino = destino.id
            WHERE
                operacao.conta_id_origem = ?
            """

        return jdbcTemplate.query(sql, OperacaoRowMapper(), id)
    }

    override fun findAllByContaDestinoAndTipoOperacao(id: String, operacao: String): List<Transaction> {
        val sql = """
            SELECT
                operacao.*,
                origem.data_hora AS origem_data_hora,
                origem.saldo AS origem_saldo,
                   destino.data_hora AS destino_data_hora,
                   destino.saldo AS destino_saldo
            FROM
                 operacao
                 INNER JOIN conta origem ON operacao.conta_id_origem = origem.id
                 INNER JOIN conta destino ON operacao.conta_id_destino = destino.id
            WHERE
                operacao.conta_id_destino = ? AND
                operacao.tipo_operacao = ?
            """

        return jdbcTemplate.query(sql, OperacaoRowMapper(), id, operacao)
    }

    override fun findAllByContaOrigemAndTipoOperacao(id: String, operacao: String): List<Transaction> {
        val sql = """
            SELECT
                operacao.*,
                origem.data_hora AS origem_data_hora,
                origem.saldo AS origem_saldo,
                   destino.data_hora AS destino_data_hora,
                   destino.saldo AS destino_saldo
            FROM
                 operacao
                 INNER JOIN conta origem ON operacao.conta_id_origem = origem.id
                 INNER JOIN conta destino ON operacao.conta_id_destino = destino.id
            WHERE
                operacao.conta_id_origem = ? AND
                operacao.tipo_operacao = ?
            """

        return jdbcTemplate.query(sql, OperacaoRowMapper(), id, operacao)
    }
}