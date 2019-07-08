package com.github.thaynarasilvapinto.repositories.extractor

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Transaction
import com.github.thaynarasilvapinto.repositories.JdbcAccountRepository
import com.github.thaynarasilvapinto.repositories.JdbcTransactionRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

open class OperacaoRowMapper : RowMapper<Transaction> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Transaction {
        val id = rs.getString(JdbcTransactionRepository.ID_COLUMN)
        val valorOperacao = rs.getDouble(JdbcTransactionRepository.VALOR_OPERACAO_COLUMN)
        val dataHoraOperacao = rs.getTimestamp(JdbcTransactionRepository.DATA_HORA_OPERACAO_COLUMN)
        val tipoOperacao = rs.getString(JdbcTransactionRepository.TIPO_OPERACAO_COLUMN)
        val contaOrigem = Account(
            id = rs.getString(JdbcTransactionRepository.CONTA_ORIGEM_CONLUMN),
            saldo = rs.getDouble(JdbcAccountRepository.ID_ALIAS_SALDO_ORIGEM_COLUMN),
            dataHora = rs.getTimestamp(JdbcAccountRepository.ID_ALIAS_DATA_ORIGEM_COLUMN).toLocalDateTime()
        )
        val contaDestino = Account(
            id = rs.getString(JdbcTransactionRepository.CONTA_DESTINO_CONLUMN),
            saldo = rs.getDouble(JdbcAccountRepository.ID_ALIAS_SALDO_DESTINO_COLUMN),
            dataHora = rs.getTimestamp(JdbcAccountRepository.ID_ALIAS_DATA_DESTINO_COLUMN).toLocalDateTime()
        )


        return Transaction(
            id =
            accountOrigin =
                accountDestination =
                value =
                transactionType =
                status =
                createDate =
                updateDate =
        )
    }


    /*
    val id = rs.getString(JdbcTransactionRepository.ID_COLUMN)
    val accountOrigin =  Account (
            id = rs.getString(JdbcTransactionRepository.ACCOUNT_ORIGIN_CONLUMN),
            balance = rs.getDouble(JdbcAccountRepository.ID_ALIAS_BALANCE_ORIGIN_COLUMN),
            createDate = rs.getTimestamp(JdbcAccountRepository.ID_ALIAS_CREATE_DATE_ORIGIN_COLUMN).toLocalDateTime()
    )
    val accountDestination Account (
            id = rs.getString(JdbcTransactionRepository.ACCOUNT_DESTINATION_CONLUMN),
            balance = rs.getDouble(JdbcAccountRepository.ID_ALIAS_BALANCE_DESTINATION_COLUMN),
            createDate = rs.getTimestamp(JdbcAccountRepository.ID_ALIAS_CREATE_DATE_DESTINATION_COLUMN).toLocalDateTime()
    )
    val value
    val transactionType
    val status
    val createDate
    val updateDate


     id =
     accountOrigin =
     accountDestination =
     value =
     transactionType =
     status =
     createDate =
     updateDate =
    */
}