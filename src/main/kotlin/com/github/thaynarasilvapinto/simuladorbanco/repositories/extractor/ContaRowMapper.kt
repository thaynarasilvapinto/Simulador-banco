package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class ContaRowMapper : RowMapper<Conta> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Conta {
        val id = rs.getInt(JdbcContaRepository.ID_COLUMN)
        val saldo = rs.getDouble(JdbcContaRepository.SALDO_COLUMN)
        val dataHora = rs.getString(JdbcContaRepository.DATA_HORA_COLUMN)


        return Conta(
                id = id,
                saldo = saldo,
                dataHora = LocalDateTime.parse(dataHora)
        )
    }
}