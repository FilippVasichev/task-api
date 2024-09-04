package com.task.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice("com.task.controller")
class GlobalHttpErrorHandler {
    @ExceptionHandler(value = [IllegalAccessException::class])
    fun processIllegalArgument(): ResponseEntity<Error> = ResponseEntity(Error("authentication failed"), HttpStatus.FORBIDDEN)
}
