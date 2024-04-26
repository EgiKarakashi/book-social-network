package com.egi.book.feedback

import com.egi.book.book.Book
import com.egi.book.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
data class Feedback(
    val note: Double,
    private val comment: String,
    @ManyToOne
    @JoinColumn(name = "book_id")
    private val book: Book,
) : BaseEntity(
        id = 0,
        createdDate = LocalDateTime.now(),
        lastModifiedDate = LocalDateTime.now(),
        createdBy = 0,
        lastModifiedBy = 0,
    )
