package com.github.thaynarasilvapinto.simuladorbanco.controller.exception

open class ValidationError(
        status: Int,
        message: String,
        var errors: MutableList<FieldMessage> = mutableListOf()
) : StandardError(status, message) {

    fun addError(fieldName: String, message: String) {
        this.errors.add(FieldMessage(fieldName, message))
    }

    companion object {
        val serialVersionUID = 1L
    }
}