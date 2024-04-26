package com.egi.book.book

data class BorrowedBookResponse(
    val id: Int,
    val title: String?,
    val authorName: String?,
    val isbn: String?,
    val rate: Double,
    val returned: Boolean?,
    val returnApproved: Boolean?,
)
