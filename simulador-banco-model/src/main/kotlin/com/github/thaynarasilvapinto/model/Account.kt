package com.github.thaynarasilvapinto.model

import java.time.LocalDateTime
import java.util.*

data class Account(
    var id: String = UUID.randomUUID().toString(),
    var customer: Customer,
    var balance: Double,
    var accountType: AccountType,
    var status: StatusAccount,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime
) {
    enum class AccountType {
        CURRENT, SAVINGS
    }
    enum class StatusAccount{
        PENDING, CANCELED, ACTIVE
    }
}