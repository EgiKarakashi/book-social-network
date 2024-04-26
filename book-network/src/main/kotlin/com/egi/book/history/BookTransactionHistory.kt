package com.egi.book.history

import com.egi.book.book.Book
import com.egi.book.common.BaseEntity
import com.egi.book.user.User
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
data class BookTransactionHistory(
    @ManyToOne
    @JoinColumn(name = "user_id")
    private val user: User,
    @ManyToOne
    @JoinColumn(name = "book_id") val book: Book,
    val returned: Boolean,
    val returnApproved: Boolean,
) : BaseEntity(
        id = 0,
        createdDate = LocalDateTime.now(),
        lastModifiedDate = LocalDateTime.now(),
        createdBy = 0,
        lastModifiedBy = 0,
    )
