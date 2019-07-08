package com.github.thaynarasilvapinto.model
import java.time.LocalDateTime
import java.util.*

data class Document(
    var id: String = UUID.randomUUID().toString(),
    var customerId: Customer,
    var number: String,
    var documentType: DocumentType,
    var createDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime
){
    enum class DocumentType {
        CPF,RG
    }
}