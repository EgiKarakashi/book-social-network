package com.egi.book.handler

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ExceptionResponse(
    val businessErrorCode: Int? = null,
    val businessErrorDescription: String? = null,
    val error: String? = null,
    var validationErrors: MutableSet<String?>? = null,
    val errors: MutableMap<String, String>? = null,
)
