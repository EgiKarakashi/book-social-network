package com.egi.book.handler

import com.egi.book.handler.BusinessErrorCodes.*
import jakarta.mail.MessagingException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(LockedException::class)
    fun handleException(exp: LockedException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(UNAUTHORIZED)
            .body(
                ExceptionResponse(
                    businessErrorCode = ACCOUNT_LOCKED.code,
                    businessErrorDescription = ACCOUNT_LOCKED.description,
                    error = exp.message,
                ),
            )
    }

    @ExceptionHandler(DisabledException::class)
    fun handleException(exp: DisabledException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(UNAUTHORIZED)
            .body(
                ExceptionResponse(
                    businessErrorCode = ACCOUNT_DISABLED.code,
                    businessErrorDescription = ACCOUNT_DISABLED.description,
                    error = exp.message,
                ),
            )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleException(exp: BadCredentialsException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(UNAUTHORIZED)
            .body(
                ExceptionResponse(
                    businessErrorCode = BAD_CREDENTIALS.code,
                    businessErrorDescription = BAD_CREDENTIALS.description,
                    error = BAD_CREDENTIALS.description,
                ),
            )
    }

    @ExceptionHandler(MessagingException::class)
    fun handleException(exp: MessagingException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(
                ExceptionResponse(
                    error = exp.message,
                ),
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exp: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(BAD_REQUEST)
            .body(
                ExceptionResponse().apply {
                    this.validationErrors = exp.allErrors.map { it.defaultMessage }.toMutableSet()
                },
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exp: Exception): ResponseEntity<ExceptionResponse> {
        exp.printStackTrace()
        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(
                ExceptionResponse(
                    businessErrorDescription = "Interval error, contact admin!",
                    error = exp.message,
                ),
            )
    }
}
