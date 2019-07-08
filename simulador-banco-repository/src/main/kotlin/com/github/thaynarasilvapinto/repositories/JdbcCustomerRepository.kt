package com.github.thaynarasilvapinto.repositories

import com.github.thaynarasilvapinto.model.Customer
import com.github.thaynarasilvapinto.model.repository.CustomerRepository
import com.github.thaynarasilvapinto.repositories.extractor.CustomerRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class JdbcCustomerRepository @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate
) : CustomerRepository {


    companion object {
        const val TABLE_NAME = "customer"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val CREATE_DATE_COLUMN = "create_date"
        const val UPDATE_DATE_COLUMN = "update_date"
    }

    override fun create(customer: Customer): Int {
        val sql = """
            insert into $TABLE_NAME ($ID_COLUMN, $NAME_COLUMN, $CREATE_DATE_COLUMN , $UPDATE_DATE_COLUMN)
                values (?, ?, ?, ?)
                """

        return jdbcTemplate.update(
            sql,
            customer.id,
            customer.name,
            LocalDateTime.now(),
            null
        )
    }

    override fun findById(id: String): Customer? {
        val sql = """
                SELECT
                  $TABLE_NAME.*
                FROM
                  $TABLE_NAME
                WHERE
                  $ID_COLUMN = ?
                """

        return try {
            jdbcTemplate.queryForObject(sql, CustomerRowMapper(), id)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun update(customer: Customer): Int {
        val sql = """
            update $TABLE_NAME set
                   $NAME_COLUMN = ?,
                   $UPDATE_DATE_COLUMN = ?
                where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(
            sql,
            customer.id,
            customer.name,
            LocalDateTime.now()
        )
    }

    override fun deleteById(id: String): Int {
        val sql = """
            delete from $TABLE_NAME where $ID_COLUMN = ?
            """
        return jdbcTemplate.update(sql, id)
    }
}