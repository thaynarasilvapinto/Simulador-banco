package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Customer

interface CustomerRepository {

    fun create(customer: Customer): Int

    fun findById(id: String): Customer?

    fun update(customer: Customer): Int

    fun deleteById(id: String): Int
}