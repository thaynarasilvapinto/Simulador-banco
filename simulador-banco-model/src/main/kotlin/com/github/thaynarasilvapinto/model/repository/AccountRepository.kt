package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Account

interface AccountRepository{

    fun create(account: Account): Int

    fun findById(id: String): Account?

    fun update(account: Account): Int

    fun updateBalance(account: Account): Int

    fun deleteById(id: String): Int
}