package com.egi.book.book

import com.egi.book.common.BaseEntity
import com.egi.book.feedback.Feedback
import com.egi.book.history.BookTransactionHistory
import com.egi.book.user.User
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Transient
import java.time.LocalDateTime
import kotlin.math.round

@Entity
data class Book(
    val title: String? = null,
    val authorName: String? = null,
    val isbn: String? = null,
    val synopsis: String? = null,
    private val bookCover: String? = null,
    val archived: Boolean? = null,
    val shareable: Boolean? = null,
    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: User? = null,
    @OneToMany(mappedBy = "book")
    private val feedbacks: MutableList<Feedback> = mutableListOf(),
    @OneToMany(mappedBy = "book")
    private val histories: MutableList<BookTransactionHistory> = mutableListOf(),
) : BaseEntity(
        id = 0,
        createdDate = LocalDateTime.now(),
        lastModifiedDate = LocalDateTime.now(),
        createdBy = 0,
        lastModifiedBy = 0,
    ) {
    @Transient
    fun getRate(): Double {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0
        }
        val rate =
            feedbacks.stream()
                .mapToDouble { it.note }
                .average()
                .orElse(0.0)
        val roundedRate = round(rate * 10.0) / 10.0

        return roundedRate
    }
}
