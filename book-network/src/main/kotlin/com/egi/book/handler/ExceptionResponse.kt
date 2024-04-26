package com.egi.book.handler

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ExceptionResponse(
    private val businessErrorCode: Int? = null,
    private val businessErrorDescription: String? = null,
    private val error: String? = null,
    private val validationErrors: MutableSet<String?> = mutableSetOf(),
    private val errors: MutableMap<String, String>? = mutableMapOf()
)
