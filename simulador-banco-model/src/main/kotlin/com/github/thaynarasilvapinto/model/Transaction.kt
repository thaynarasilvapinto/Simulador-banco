package com.github.thaynarasilvapinto.model

import java.time.LocalDateTime
import java.util.*


data class Transaction(
    var id: String = UUID.randomUUID().toString(),
    var accountOrigin: Account,
    var accountDestination: Account,
    var value: Double,
    var transactionType: TransactionType,
    var status: TransactionStatus,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime
) {
    enum class TransactionType {
        WITHDRAW, DEPOSIT, TRANSFER, TRANSFER_RECEIVEMENT
    }
    enum class TransactionStatus {
        PENDING, CANCELED, ACTIVE
    }
}