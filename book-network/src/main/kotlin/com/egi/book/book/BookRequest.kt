package com.egi.book.book

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class BookRequest(
    val id: Int,
    @field:NotNull(message = "100")
    @field:NotEmpty(message = "100")
    val title: String,
    @field:NotNull(message = "101")
    @field:NotEmpty(message = "101")
    val authorName: String,
    @field:NotNull(message = "102")
    @field:NotEmpty(message = "102")
    private val isbn: String,
    @field:NotNull(message = "103")
    @field:NotEmpty(message = "103")
    val synopsis: String,
    val shareable: Boolean,
)
