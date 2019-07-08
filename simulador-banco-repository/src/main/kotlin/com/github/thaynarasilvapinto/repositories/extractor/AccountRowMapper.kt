package com.github.thaynarasilvapinto.repositories.extractor

import com.github.thaynarasilvapinto.model.Account
import com.github.thaynarasilvapinto.model.Customer
import com.github.thaynarasilvapinto.repositories.JdbcAccountRepository
import com.github.thaynarasilvapinto.repositories.JdbcCustomerRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

open class AccountRowMapper : RowMapper<Account> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Account {
        val id = rs.getString(JdbcAccountRepository.ID_COLUMN)
        val balance = rs.getDouble(JdbcAccountRepository.BALANCE_CALUMN)
        val customer = Customer(
            id = rs.getString(JdbcAccountRepository.CUSTOMER_ID),
            name = rs.getString(JdbcCustomerRepository.NAME_COLUMN),
            createDate = rs.getTimestamp(JdbcCustomerRepository.CREATE_DATE_COLUMN).toLocalDateTime(),
            updateDate = rs.getTimestamp(JdbcCustomerRepository.UPDATE_DATE_COLUMN).toLocalDateTime()
        )
        val accountType = rs.getString(JdbcAccountRepository.ACCOUNT_TYPE_COLUMN)
        val status = rs.getString(JdbcAccountRepository.STATUS_COLUMN)
        val createDate = rs.getTimestamp(JdbcAccountRepository.CREATE_DATE_COLUMN).toLocalDateTime()
        val updateDate = rs.getTimestamp(JdbcAccountRepository.UPDATE_DATE_COLUMN).toLocalDateTime()


        return Account(
            id = id,
            customer = customer,
            balance = balance,
            accountType = enumValueOf(accountType),
            status = enumValueOf(status),
            createDate = createDate,
            updateDate = updateDate
        )
    }
}
