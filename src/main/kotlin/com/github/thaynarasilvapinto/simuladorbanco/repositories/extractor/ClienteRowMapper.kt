package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcClienteRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class ClienteRowMapper : RowMapper<Cliente> {


    override fun mapRow(rs: ResultSet, rowNum: Int): Cliente {
        val id = rs.getInt(JdbcClienteRepository.ID_COLUMN)
        val name = rs.getString(JdbcClienteRepository.NAME_COLUMN)
        val cpf = rs.getString(JdbcClienteRepository.CPF_COLUMN)
        val dataHora = rs.getString(JdbcClienteRepository.DATA_HORA_COLUMN)
        val conta = rs.getInt(JdbcClienteRepository.CONTA_ID_COLUMN)


        return Cliente(
                id = id,
                nome = name,
                cpf = cpf,
                dataHora = LocalDateTime.parse(dataHora),
                conta = conta
        )
    }
}