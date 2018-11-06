package com.github.thaynarasilvapinto.simuladorbanco.repositories

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.domain.repository.OperacaoRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor.OperacaoRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
open class JdbcOperacaoRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : OperacaoRepository {

    var id = 0

    companion object {
        const val TABLE_NAME = "operacao"
        const val ID_COLUMN = "id"
        const val VALOR_OPERACAO_COLUMN = "valor_operacao"
        const val DATA_HORA_OPERACAO_COLUMN = "data_hora_operacao"
        const val TIPO_OPERACAO_COLUMN = "tipo_operacao"
        const val CONTA_ORIGEM_CONLUMN = "conta_id_origem"
        const val CONTA_DESTINO_CONLUMN = "conta_id_destino"
    }

    override fun save(operacao: Operacao): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $VALOR_OPERACAO_COLUMN, $DATA_HORA_OPERACAO_COLUMN, $TIPO_OPERACAO_COLUMN, $CONTA_ORIGEM_CONLUMN, $CONTA_DESTINO_CONLUMN)
                values (id,?,?,?,?,?)
                """

        id++

        return jdbcTemplate.update(
                sql,
                operacao.idOperacao,
                operacao.valorOperacao,
                operacao.dataHoraOperacao,
                operacao.tipoOperacao,
                operacao.contaOrigem,
                operacao.contaDestino
        )
    }

    override fun findById(operacaoId: Int): Optional<Operacao> {
        val sql = """
            select * from $TABLE_NAME where $ID_COLUMN = ?
            """
        var operacao: Optional<Operacao> = Optional.ofNullable(jdbcTemplate.queryForObject(sql, OperacaoRowMapper(), operacaoId))
        return operacao
    }

    override fun update(operacao: Operacao): Int {
        val sql = """
            update $TABLE_NAME set $ID_COLUMN = ?, $VALOR_OPERACAO_COLUMN = ?,
                   $DATA_HORA_OPERACAO_COLUMN = ?, $TIPO_OPERACAO_COLUMN = ?,
                   $CONTA_ORIGEM_CONLUMN = ?, $CONTA_DESTINO_CONLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
                sql,
                operacao.idOperacao,
                operacao.valorOperacao,
                operacao.dataHoraOperacao,
                operacao.tipoOperacao,
                operacao.contaOrigem,
                operacao.contaDestino
        )
    }

    override fun deleteById(id: Int): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }

    override fun findAllByContaOrigem(conta: Conta): List<Operacao> {
        val sql = """
            select * from $TABLE_NAME where $CONTA_ORIGEM_CONLUMN = ?
            """

        var contas = sql.map { jdbcTemplate.queryForObject(sql, OperacaoRowMapper(), conta.id) }

        return contas
    }

    override fun findAllByContaDestinoAndTipoOperacao(conta: Conta, operacao: Operacao.TipoOperacao): List<Operacao> {
        val sql = """
            select * from $TABLE_NAME where $CONTA_ORIGEM_CONLUMN = ? and $TIPO_OPERACAO_COLUMN = ?
            """

        var listaOperacaoes = sql.map { jdbcTemplate.queryForObject(sql, OperacaoRowMapper(), conta.id) }
        return listaOperacaoes
    }
}