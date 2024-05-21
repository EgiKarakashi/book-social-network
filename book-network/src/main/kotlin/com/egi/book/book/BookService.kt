package com.egi.book.book

import com.egi.book.book.file.FileStorageService
import com.egi.book.common.PageResponse
import com.egi.book.exception.OperationNotPermittedException
import com.egi.book.history.BookTransactionHistory
import com.egi.book.history.BookTransactionHistoryRepository
import com.egi.book.user.User
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Objects
import java.util.stream.Collectors

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val bookTransactionHistoryRepository: BookTransactionHistoryRepository,
    private val bookMapper: BookMapper,
    private val fileStorageService: FileStorageService,
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

    fun findAllBorrowedBooks(
        page: Int,
        size: Int,
        connectedUser: Authentication,
    ): PageResponse<BorrowedBookResponse>? {
        val user = connectedUser.principal as User
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.id)
        val bookResponse: MutableList<BorrowedBookResponse> =
            allBorrowedBooks.stream()
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

    fun findAllReturnedBooks(
        page: Int,
        size: Int,
        connectedUser: Authentication,
    ): PageResponse<BorrowedBookResponse>? {
        val user = connectedUser.principal as User
        val pageable: Pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.id)
        val bookResponse: MutableList<BorrowedBookResponse> =
            allBorrowedBooks.stream()
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

    fun updateShareableStatus(
        bookId: Int,
        connectedUser: Authentication,
    ): Int? {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        val user = connectedUser.principal as User
        if (!Objects.equals(book.owner?.id, user.id)) {
            throw OperationNotPermittedException("You cannot update books shareable status")
        }
        book.shareable = !book.shareable!!
        bookRepository.save(book)
        return bookId
    }

    fun updateArchivedStatus(
        bookId: Int,
        connectedUser: Authentication,
    ): Int? {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        val user = connectedUser.principal as User
        if (!Objects.equals(book.owner?.id, user.id)) {
            throw OperationNotPermittedException("You cannot update books archived status")
        }
        book.archived = !book.archived!!
        bookRepository.save(book)
        return bookId
    }

    fun borrowBook(
        bookId: Int,
        connectedUser: Authentication,
    ): Int? {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        if (book.archived == true || !book.shareable!!) {
            throw OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable")
        }
        val user = connectedUser.principal as User
        if (Objects.equals(book.owner?.id, user.id)) {
            throw OperationNotPermittedException("You cannot borrow your own book")
        }
        val isAlreadyBorrowed: Boolean? = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.id)
        if (isAlreadyBorrowed == true) {
            throw OperationNotPermittedException("The requested book is already borrowed")
        }
        val bookTransactionHistory =
            BookTransactionHistory(
                user = user,
                book = book,
                returned = false,
                returnApproved = false,
            )
        return bookTransactionHistoryRepository.save(bookTransactionHistory).id
    }

    fun returnBorrowedBook(
        bookId: Int,
        connectedUser: Authentication,
    ): Int? {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        if (book.archived == true || !book.shareable!!) {
            throw OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable")
        }
        val user = connectedUser.principal as User
        if (Objects.equals(book.owner?.id, user.id)) {
            throw OperationNotPermittedException("You cannot borrow or return your own book")
        }
        val bookTransactionHistory =
            bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.id).orElseThrow {
                OperationNotPermittedException("You did not borrow this book")
            }
        bookTransactionHistory.returned = true
        return bookTransactionHistoryRepository.save(bookTransactionHistory).id
    }

    fun approveReturnBorrowedBook(
        bookId: Int,
        connectedUser: Authentication,
    ): Int? {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        if (book.archived == true || !book.shareable!!) {
            throw OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable")
        }
        val user = connectedUser.principal as User
        if (Objects.equals(book.owner?.id, user.id)) {
            throw OperationNotPermittedException("You cannot borrow or return your own book")
        }
        val bookTransactionHistory =
            bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.id).orElseThrow {
                OperationNotPermittedException("The book is not returned yet, cannot approve its returned.")
            }
        bookTransactionHistory.returnApproved = true
        return bookTransactionHistoryRepository.save(bookTransactionHistory).id
    }

    fun uploadBookCoverPicture(
        file: MultipartFile,
        connectedUser: Authentication,
        bookId: Int,
    ) {
        val book =
            bookRepository.findById(bookId)
                .orElseThrow { EntityNotFoundException("No book found with the ID:: $bookId") }
        val user = connectedUser.principal as User
        val bookCover = fileStorageService.saveFile(file, book, user.id)
        book.bookCover = bookCover
        bookRepository.save(book)
    }
}
