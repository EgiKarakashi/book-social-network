package com.egi.book.book

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Int>, JpaSpecificationExecutor<Book> {
    @Query("SELECT book FROM Book book WHERE book.archived = false AND book.shareable = true AND book.owner.id != :userId")
    fun findAllDisplayableBooks(
        pageable: Pageable,
        userId: Int,
    ): Page<Book>
}
