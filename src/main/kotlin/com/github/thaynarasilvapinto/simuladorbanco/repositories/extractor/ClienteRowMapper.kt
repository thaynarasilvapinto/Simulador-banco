package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.domain.Conta
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcClienteRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

open class ClienteRowMapper : RowMapper<Cliente> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Cliente {
        val id = rs.getString(JdbcClienteRepository.ID_COLUMN)
        val name = rs.getString(JdbcClienteRepository.NAME_COLUMN)
        val cpf = rs.getString(JdbcClienteRepository.CPF_COLUMN)
        val dataHora = rs.getTimestamp(JdbcClienteRepository.DATA_HORA_COLUMN)
        val conta = Conta(
            id = rs.getString(JdbcContaRepository.ID_ALIAS_COLUMN),
            saldo = rs.getDouble(JdbcContaRepository.SALDO_COLUMN),
            dataHora = rs.getTimestamp(JdbcContaRepository.DATA_HORA_COLUMN).toLocalDateTime()
        )


        return Cliente(
            id = id,
            nome = name,
            cpf = cpf,
            dataHora = dataHora.toLocalDateTime(),
            conta = conta
        )
    }
}