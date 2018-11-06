package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcOperacaoRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class OperacaoRowMapper : RowMapper<Operacao> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Operacao {
        val id = rs.getInt(JdbcOperacaoRepository.ID_COLUMN)
        val valorOperacao = rs.getDouble(JdbcOperacaoRepository.VALOR_OPERACAO_COLUMN)
        val dataHoraOperacao = rs.getString(JdbcOperacaoRepository.DATA_HORA_OPERACAO_COLUMN)
        val tipoOperacao = rs.getString(JdbcOperacaoRepository.TIPO_OPERACAO_COLUMN)
        val contaOrigem = rs.getInt(JdbcOperacaoRepository.CONTA_ORIGEM_CONLUMN)
        val contaDestino = rs.getInt(JdbcOperacaoRepository.CONTA_DESTINO_CONLUMN)


        return Operacao(
                idOperacao = id,
                valorOperacao = valorOperacao,
                dataHoraOperacao = LocalDateTime.parse(dataHoraOperacao),
                tipoOperacao = enumValueOf(tipoOperacao),
                contaOrigem = contaOrigem,
                contaDestino = contaDestino
        )
    }
}