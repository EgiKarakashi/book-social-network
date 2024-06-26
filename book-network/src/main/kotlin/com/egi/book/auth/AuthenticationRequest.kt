package com.egi.book.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class AuthenticationRequest(
    @field:Email(message = "Email is not well formatted")
    @field:NotEmpty(message = "Email is mandatory")
    @field:NotBlank(message = "Email is mandatory")
    val email: String,
    @field:Size(min = 8, message = "Password should be more than 8 characters")
    @field:NotEmpty(message = "Password is mandatory")
    @field:NotBlank(message = "Password is mandatory")
    val password: String,
)
