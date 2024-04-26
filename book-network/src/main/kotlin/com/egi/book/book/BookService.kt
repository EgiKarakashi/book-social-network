package com.egi.book.book

import com.egi.book.common.PageResponse
import com.egi.book.history.BookTransactionHistoryRepository
import com.egi.book.user.User
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val bookTransactionHistoryRepository: BookTransactionHistoryRepository,
    private val bookMapper: BookMapper,
) {
    fun save(
        request: BookRequest,
        connectedUser: Authentication,
    ): Int? {
        val user = connectedUser.principal as User
        val book = bookMapper.toBook(request) as Book
        book.owner = user
        return bookRepository.save(book).id
    }

    fun findById(bookId: Int): BookResponse {
        return bookRepository.findById(bookId)
            .map { bookMapper.toBookResponse(it) }
            .orElseThrow { EntityNotFoundException("No book found with id $bookId") }
    }

    fun findAllBooks(
        page: Int,
        size: Int,
        connectedUser: Authentication,
    ): PageResponse<BookResponse>? {
        val user = connectedUser.principal as User
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val books = bookRepository.findAllDisplayableBooks(pageable, user.id)
        val bookResponse =
            books.stream()
                .map { bookMapper.toBookResponse(it) }
                .collect(Collectors.toList())
        return PageResponse(
            bookResponse,
            books.number,
            books.size,
            books.totalElements,
            books.totalPages,
            books.isFirst,
            books.isLast,
        )
    }

    fun findAllBooksByOwner(
        page: Int,
        size: Int,
        connectedUser: Authentication,
    ): PageResponse<BookResponse>? {
        val user = connectedUser.principal as User
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val books = bookRepository.findAll(BookSpecification.withOwnerId(user.id), pageable)
        val bookResponse =
            books.stream()
                .map { bookMapper.toBookResponse(it) }
                .collect(Collectors.toList())
        return PageResponse(
            bookResponse,
            books.number,
            books.size,
            books.totalElements,
            books.totalPages,
            books.isFirst,
            books.isLast,
        )
    }

    fun findAllBorrowedBooks(page: Int, size: Int, connectedUser: Authentication): PageResponse<BorrowedBookResponse>? {
        val user = connectedUser.principal as User
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.id)
        val bookResponse: MutableList<BorrowedBookResponse> = allBorrowedBooks.stream()
            .map { bookMapper.toBorrowedBookResponse(it) }
            .toList()
        return PageResponse(
            bookResponse,
            allBorrowedBooks.number,
            allBorrowedBooks.size,
            allBorrowedBooks.totalElements,
            allBorrowedBooks.totalPages,
            allBorrowedBooks.isFirst,
            allBorrowedBooks.isLast,
        )
    }
}
