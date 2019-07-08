package com.github.thaynarasilvapinto.repositories

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.repository.AccountRepository
import com.github.thaynarasilvapinto.repositories.extractor.AccountRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class JdbcAccountRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : AccountRepository {


    companion object {
        const val TABLE_NAME = "account"
        const val ID_COLUMN = "id"
        const val CUSTOMER_ID = "customer_id"
        const val BALANCE_CALUMN = "balance"
        const val ACCOUNT_TYPE_COLUMN = "account_type"
        const val STATUS_COLUMN = "status"
        const val CREATE_DATE_COLUMN = "create_date"
        const val UPDATE_DATE_COLUMN = "update_date"

        const val ID_ALIAS_COLUMN = "conta_id"
        const val ID_ALIAS_SALDO_ORIGEM_COLUMN = "origem_saldo"
        const val ID_ALIAS_DATA_ORIGEM_COLUMN = "origem_data_hora"
        const val ID_ALIAS_SALDO_DESTINO_COLUMN = "destino_saldo"
        const val ID_ALIAS_DATA_DESTINO_COLUMN = "destino_data_hora"
    }

    override fun create(account: Account): Int {
        val sql = """
            insert into $TABLE_NAME
            ($ID_COLUMN,
             $CUSTOMER_ID,
             $BALANCE_CALUMN,
             $ACCOUNT_TYPE_COLUMN,
             $STATUS_COLUMN,
             $CREATE_DATE_COLUMN,
             $UPDATE_DATE_COLUMN)
                values (?,?,?,?,?,?,?)
                """

        return jdbcTemplate.update(
            sql,
            account.id,
            account.customer,
            account.balance,
            account.accountType,
            account.status,
            LocalDateTime.now(),
            null
        )
    }

    override fun findById(id: String): Account? {
        val sql = """
            select * from $TABLE_NAME where $ID_COLUMN = ?
            """
        return try {
            jdbcTemplate.queryForObject(sql, AccountRowMapper(), id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun update(account: Account): Int {
        val sql = """
            update $TABLE_NAME set
             $CUSTOMER_ID = ?,
             $ACCOUNT_TYPE_COLUMN = ?,
             $STATUS_COLUMN = ?,
             $UPDATE_DATE_COLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
            sql,
            account.customer.id,
            account.accountType,
            account.status,
            LocalDateTime.now(),
            account.id
        )
    }


    override fun updateBalance(account: Account): Int {
        val sql = """
            update $TABLE_NAME set $BALANCE_CALUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
            sql,
            account.balance,
            account.id
        )
    }

    override fun deleteById(id: String): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }
}