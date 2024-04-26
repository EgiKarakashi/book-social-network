package com.egi.book.book

import com.egi.book.history.BookTransactionHistory
import org.springframework.stereotype.Service

@Service
class BookMapper {
    fun toBook(request: BookRequest): Any {
        return Book(
            title = request.title,
            authorName = request.authorName,
            synopsis = request.synopsis,
            archived = false,
            shareable = request.shareable,
        ).apply {
            id = request.id
        }
    }

    fun toBookResponse(book: Book): BookResponse {
        return BookResponse(
            id = book.id,
            title = book.title,
            authorName = book.authorName,
            isbn = book.isbn,
            synopsis = book.synopsis,
            rate = book.getRate(),
            archived = book.archived,
            shareable = book.shareable,
            owner = book.owner?.fullName(),
            // TODO implement later
//            cover = FileU
        )
    }

    fun toBorrowedBookResponse(history: BookTransactionHistory): BorrowedBookResponse {
        return BorrowedBookResponse(
            id = history.book.id,
            title = history.book.title,
            authorName = history.book.authorName,
            isbn = history.book.isbn,
            rate = history.book.getRate(),
            returned = history.returned,
            returnApproved = history.returnApproved
        )
    }
}
