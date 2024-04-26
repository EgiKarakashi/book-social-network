package com.egi.book.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
data class Token(
    @Id
    @GeneratedValue
    private val id: Int = 0,
    private val token: String = "",
    private val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime = LocalDateTime.now(),
    var validatedAt: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    val user: User?,
)
