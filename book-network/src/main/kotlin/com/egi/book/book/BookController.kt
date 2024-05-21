package com.egi.book.book

import com.egi.book.common.PageResponse
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("books")
@Tag(name = "book")
class BookController(
    private val service: BookService,
) {
    @PostMapping
    fun save(
        @Valid @RequestBody request: BookRequest,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.save(request, connectedUser))
    }

    @GetMapping("{book_id}")
    fun findBookById(
        @PathVariable("book_id") bookId: Int,
    ): ResponseEntity<BookResponse> {
        return ResponseEntity.ok(service.findById(bookId))
    }

    @GetMapping
    fun findAllBooks(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int,
        connectedUser: Authentication,
    ): ResponseEntity<PageResponse<BookResponse>> {
        return ResponseEntity.ok(service.findAllBooks(page, size, connectedUser))
    }

    @GetMapping("/owner")
    fun findAllBooksByOwner(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int,
        connectedUser: Authentication,
    ): ResponseEntity<PageResponse<BookResponse>> {
        return ResponseEntity.ok(service.findAllBooksByOwner(page, size, connectedUser))
    }

    @GetMapping("/borrowed")
    fun findAllBorrowedBooks(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int,
        connectedUser: Authentication,
    ): ResponseEntity<PageResponse<BorrowedBookResponse>> {
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser))
    }

    @GetMapping("/returned")
    fun findAllReturnedBooks(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "10", required = false) size: Int,
        connectedUser: Authentication,
    ): ResponseEntity<PageResponse<BorrowedBookResponse>> {
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size, connectedUser))
    }

    @PatchMapping("/shareable/{book-id}")
    fun updateShareableStatus(
        @PathVariable("book-id") bookId: Int,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser))
    }

    @PatchMapping("/archived/{book-id}")
    fun updateArchivedStatus(
        @PathVariable("book-id") bookId: Int,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.updateArchivedStatus(bookId, connectedUser))
    }

    @PostMapping("/borrow/{book-id}")
    fun borrowBook(
        @PathVariable("book-id") bookId: Int,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser))
    }

    @PatchMapping("/borrow/return/{book-id}")
    fun returnBorrowBook(
        @PathVariable("book-id") bookId: Int,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.returnBorrowedBook(bookId, connectedUser))
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    fun approveReturnBorrowBook(
        @PathVariable("book-id") bookId: Int,
        connectedUser: Authentication,
    ): ResponseEntity<Int> {
        return ResponseEntity.ok(service.approveReturnBorrowedBook(bookId, connectedUser))
    }

    @PostMapping("/cover/{book-id}", consumes = ["multipart/form-data"])
    fun uploadBookCoverPicture(
        @PathVariable("book-id") bookId: Int,
        @Parameter()
        @RequestPart("file") file: MultipartFile,
        connectedUser: Authentication,
    ): ResponseEntity<*> {
        service.uploadBookCoverPicture(file, connectedUser, bookId)
        return ResponseEntity.accepted().build<Void>()
    }
}
