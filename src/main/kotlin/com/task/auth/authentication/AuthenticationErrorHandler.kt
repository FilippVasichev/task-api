package com.task.auth.authentication

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice("com.task.auth.authentication")
class AuthenticationErrorHandler {
    @ExceptionHandler(value = [IllegalArgumentException::class, NoSuchElementException::class])
    fun processIllegalArgument(): ResponseEntity<Error> = ResponseEntity(Error("login or password is incorrect"), HttpStatus.FORBIDDEN)
}
