package com.egi.book.history

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookTransactionHistoryRepository: JpaRepository<BookTransactionHistory, Int> {
    @Query("SELECT history FROM BookTransactionHistory history WHERE history.user.id = :userId")
    fun findAllBorrowedBooks(pageable: Pageable, userId: Int): Page<BookTransactionHistory>
}