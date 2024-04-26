package com.egi.book.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class AuthenticationRequest(
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    val email: String,
    @Size(min = 8, message = "Password should be more than 8 characters")
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    val password: String,
)
