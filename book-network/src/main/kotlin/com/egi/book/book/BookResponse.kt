package com.egi.book.book

data class BookResponse(
    val id: Int,
    val title: String?,
    val authorName: String?,
    val isbn: String?,
    val synopsis: String?,
    val owner: String?,
    val cover: ByteArray? = null,
    val rate: Double,
    val archived: Boolean?,
    val shareable: Boolean?,
)
