package com.egi.book.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class RegistrationRequest(
    @field:NotEmpty(message = "Firstname is mandatory")
    @field:NotBlank(message = "Firstname is mandatory")
    val firstname: String,
    @field:NotEmpty(message = "Lastname is mandatory")
    @field:NotBlank(message = "Lastname is mandatory")
    val lastname: String,
    @field:Email(message = "Email is not well formatted")
    @field:NotEmpty(message = "Email is mandatory")
    @field:NotBlank(message = "Email is mandatory")
    val email: String,
    @field:Size(min = 8, message = "Password should be more than 8 characters")
    @field:NotEmpty(message = "Password is mandatory")
    @field:NotBlank(message = "Password is mandatory")
    val password: String,
)
