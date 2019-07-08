package com.github.thaynarasilvapinto.model

import java.time.LocalDateTime
import java.util.*


data class Customer(
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime
)