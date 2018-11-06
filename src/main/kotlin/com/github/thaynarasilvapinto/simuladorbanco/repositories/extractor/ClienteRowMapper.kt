package com.github.thaynarasilvapinto.simuladorbanco.repositories.extractor

import com.github.thaynarasilvapinto.simuladorbanco.domain.Cliente
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcClienteRepository
import com.github.thaynarasilvapinto.simuladorbanco.repositories.JdbcContaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDateTime

@Component
class ClienteRowMapper : RowMapper<Cliente> {

    @Autowired
    private lateinit var repository: JdbcContaRepository

    override fun mapRow(rs: ResultSet, rowNum: Int): Cliente {
        val id = rs.getInt(JdbcClienteRepository.ID_COLUMN)
        val name = rs.getString(JdbcClienteRepository.NAME_COLUMN)
        val cpf = rs.getString(JdbcClienteRepository.CPF_COLUMN)
        val dataHora = rs.getString(JdbcClienteRepository.DATA_HORA_COLUMN)
        val contaId = rs.getInt(JdbcClienteRepository.CONTA_ID_COLUMN)
        val conta = repository.findById(contaId)


        return Cliente(
                id = id,
                nome = name,
                cpf = cpf,
                dataHora = LocalDateTime.parse(dataHora),
                conta = conta.get()
        )
    }
}