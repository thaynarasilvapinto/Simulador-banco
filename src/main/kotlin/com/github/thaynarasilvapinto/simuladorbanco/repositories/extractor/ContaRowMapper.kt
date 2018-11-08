package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

open class ContaRowMapper : RowMapper<Conta> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Conta {
        val id = rs.getString(JdbcContaRepository.ID_COLUMN)
        val saldo = rs.getDouble(JdbcContaRepository.SALDO_COLUMN)
        val dataHora = rs.getTimestamp(JdbcContaRepository.DATA_HORA_COLUMN)


        return Conta(
            id = id,
            saldo = saldo,
            dataHora = dataHora.toLocalDateTime()
        )
    }
}