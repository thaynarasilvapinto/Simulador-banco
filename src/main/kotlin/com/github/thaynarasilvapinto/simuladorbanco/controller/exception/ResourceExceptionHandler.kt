package com.github.thaynarasilvapinto.simuladorbanco.controller.exception

import com.github.thaynarasilvapinto.simuladorbanco.services.exception.AccountIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.BalanceIsInsufficientException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.ClientIsValidException
import com.github.thaynarasilvapinto.simuladorbanco.services.exception.CpfIsValidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
open class ResourceExceptionHandler {

    @ExceptionHandler(ClientIsValidException::class)
    fun objectNotFound(e: ClientIsValidException): ResponseEntity<StandardError> {
        val erro = StandardError(HttpStatus.UNPROCESSABLE_ENTITY.value(), message = e.message!!)
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro)
    }

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