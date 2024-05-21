package com.egi.book.history

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BookTransactionHistoryRepository : JpaRepository<BookTransactionHistory, Int> {
    @Query("SELECT history FROM BookTransactionHistory history WHERE history.user.id = :userId")
    fun findAllBorrowedBooks(
        pageable: Pageable,
        userId: Int,
    ): Page<BookTransactionHistory>

    @Query("SELECT history FROM BookTransactionHistory history WHERE history.book.owner.id = :userId")
    fun findAllReturnedBooks(
        pageable: Pageable,
        userId: Int,
    ): Page<BookTransactionHistory>

    @Query(
        """
        SELECT (COUNT(*) > 0) AS isBorrowed 
        FROM BookTransactionHistory history 
        WHERE history.user.id = :userId 
        AND history.book.id = :bookId
        AND history.returnApproved = false
        """,
    )
    fun isAlreadyBorrowedByUser(
        bookId: Int,
        userId: Int,
    ): Boolean?

    @Query(
        """
        SELECT transaction
        FROM BookTransactionHistory transaction
        WHERE transaction.user.id = :userId
        AND transaction.book.id = :bookId
        AND transaction.returned = false
        AND transaction.returnApproved = false
    """,
    )
    fun findByBookIdAndUserId(
        @Param("bookId") bookId: Int,
        @Param("userId") id: Int,
    ): Optional<BookTransactionHistory>

    @Query(
        """
        SELECT transaction
        FROM BookTransactionHistory transaction
        WHERE transaction.book.owner.id = :ownerId
        AND transaction.book.id = :bookId
        AND transaction.returned = true
        AND transaction.returnApproved = false
    """,
    )
    fun findByBookIdAndOwnerId(
        @Param("bookId") bookId: Int,
        @Param("ownerId") id: Int,
    ): Optional<BookTransactionHistory>
}
