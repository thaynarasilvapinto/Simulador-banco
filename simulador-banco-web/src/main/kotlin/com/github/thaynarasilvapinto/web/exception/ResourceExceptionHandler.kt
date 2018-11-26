package com.github.thaynarasilvapinto.web.exception

import com.github.thaynarasilvapinto.service.exception.AccountIsValidException
import com.github.thaynarasilvapinto.service.exception.BalanceIsInsufficientException
import com.github.thaynarasilvapinto.service.exception.CpfIsValidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
open class ResourceExceptionHandler {

    @ExceptionHandler(AccountIsValidException::class)
    fun objectNotFound(e: AccountIsValidException): ResponseEntity<StandardError> {
        val erro = StandardError(HttpStatus.UNPROCESSABLE_ENTITY.value(), message = e.message!!)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro)
    }

    @ExceptionHandler(BalanceIsInsufficientException::class)
    fun objectNotFound(e: BalanceIsInsufficientException): ResponseEntity<StandardError> {
        val erro = StandardError(HttpStatus.UNPROCESSABLE_ENTITY.value(), message = e.message!!)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro)
    }

    @ExceptionHandler(CpfIsValidException::class)
    fun objectNotFound(e: CpfIsValidException): ResponseEntity<StandardError> {
        val erro = StandardError(HttpStatus.UNPROCESSABLE_ENTITY.value(), message = e.message!!)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun objectNotFound(e: MethodArgumentNotValidException): ResponseEntity<StandardError> {
        var erro = ValidationError(HttpStatus.BAD_REQUEST.value(), "Invalid Argument!")
        for (x in e.bindingResult.fieldErrors) {
            erro.addError(x.field, x.defaultMessage!!)
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro)
    }

}