package com.egi.book.common

data class PageResponse<T>(
    private val content: MutableList<T>,
    private val number: Int,
    private val size: Int,
    private val totalElements: Long,
    private val totalPages: Int,
    private val first: Boolean,
    private val last: Boolean,
)
