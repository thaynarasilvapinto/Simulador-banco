package com.github.thaynarasilvapinto.model.repository

import com.github.thaynarasilvapinto.model.Document

interface DocumentRepository {

    fun save(document: Document): Int

    fun findById(id: String): Document?

    fun update(document: Document): Int

    fun deleteById(id: String): Int

    fun findByCustomerId(customerId: String): Document?
}