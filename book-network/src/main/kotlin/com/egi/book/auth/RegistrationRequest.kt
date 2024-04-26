package com.egi.book.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class RegistrationRequest(
    @NotEmpty(message = "Firstname is mandatory")
    @NotBlank(message = "Firstname is mandatory")
    val firstname: String,
    @NotEmpty(message = "Lastname is mandatory")
    @NotBlank(message = "Lastname is mandatory")
    val lastname: String,
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    val email: String,
    @Size(min = 8, message = "Password should be more than 8 characters")
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    val password: String,
)
