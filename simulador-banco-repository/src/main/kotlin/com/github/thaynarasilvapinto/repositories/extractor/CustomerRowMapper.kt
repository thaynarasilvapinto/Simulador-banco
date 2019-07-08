package com.github.thaynarasilvapinto.repositories.extractor

import com.github.thaynarasilvapinto.model.Customer
import com.github.thaynarasilvapinto.repositories.JdbcCustomerRepository
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

open class CustomerRowMapper : RowMapper<Customer> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Customer {
        val id = rs.getString(JdbcCustomerRepository.ID_COLUMN)
        val name = rs.getString(JdbcCustomerRepository.NAME_COLUMN)
        val createDate = rs.getTimestamp(JdbcCustomerRepository.CREATE_DATE_COLUMN)
        val updateDate = rs.getTimestamp(JdbcCustomerRepository.UPDATE_DATE_COLUMN)


        return Customer(
            id = id,
            name = name,
            createDate = createDate.toLocalDateTime(),
            updateDate = updateDate.toLocalDateTime()
        )
    }
}