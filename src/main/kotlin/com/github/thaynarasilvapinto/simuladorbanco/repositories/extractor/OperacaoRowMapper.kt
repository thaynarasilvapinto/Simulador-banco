package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Operacao
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcOperacaoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime

@Component
class OperacaoRowMapper : RowMapper<Operacao> {

    @Autowired
    private lateinit var repository: JdbcContaRepository

    override fun mapRow(rs: ResultSet, rowNum: Int): Operacao {
        val id = rs.getInt(JdbcOperacaoRepository.ID_COLUMN)
        val valorOperacao = rs.getDouble(JdbcOperacaoRepository.VALOR_OPERACAO_COLUMN)
        val dataHoraOperacao = rs.getString(JdbcOperacaoRepository.DATA_HORA_OPERACAO_COLUMN)
        val tipoOperacao = rs.getString(JdbcOperacaoRepository.TIPO_OPERACAO_COLUMN)
        val contaOrigemId = rs.getInt(JdbcOperacaoRepository.CONTA_ORIGEM_CONLUMN)
        val contaOrigem = repository.findById(contaOrigemId)
        val contaDestinoId = rs.getInt(JdbcOperacaoRepository.CONTA_DESTINO_CONLUMN)
        val contaDestino = repository.findById(contaDestinoId)


        return Operacao(
                idOperacao = id,
                valorOperacao = valorOperacao,
                dataHoraOperacao = LocalDateTime.parse(dataHoraOperacao),
                tipoOperacao = enumValueOf(tipoOperacao),
                contaOrigem = contaOrigem.get(),
                contaDestino = contaDestino.get()
        )
    }
}