package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcOperacaoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime

open class OperacaoRowMapper : RowMapper<Operacao> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Operacao {
        val id = rs.getInt(JdbcOperacaoRepository.ID_COLUMN)
        val valorOperacao = rs.getDouble(JdbcOperacaoRepository.VALOR_OPERACAO_COLUMN)
        val dataHoraOperacao = rs.getTimestamp(JdbcOperacaoRepository.DATA_HORA_OPERACAO_COLUMN)
        val tipoOperacao = rs.getString(JdbcOperacaoRepository.TIPO_OPERACAO_COLUMN)
        val contaOrigem = Conta(
            id = rs.getInt(JdbcOperacaoRepository.CONTA_ORIGEM_CONLUMN),
            saldo = rs.getDouble(JdbcContaRepository.ID_ALIAS_SALDO_ORIGEM_COLUMN),
            dataHora = rs.getTimestamp(JdbcContaRepository.ID_ALIAS_DATA_ORIGEM_COLUMN).toLocalDateTime()
        )
        val contaDestino = Conta(
            id = rs.getInt(JdbcOperacaoRepository.CONTA_DESTINO_CONLUMN),
            saldo = rs.getDouble(JdbcContaRepository.ID_ALIAS_SALDO_DESTINO_COLUMN),
            dataHora = rs.getTimestamp(JdbcContaRepository.ID_ALIAS_DATA_DESTINO_COLUMN).toLocalDateTime()
        )


        return Operacao(
                idOperacao = id,
                valorOperacao = valorOperacao,
                dataHoraOperacao = dataHoraOperacao.toLocalDateTime(),
                tipoOperacao = enumValueOf(tipoOperacao),
                contaOrigem = contaOrigem,
                contaDestino = contaDestino
        )
    }
}