package com.egi.book.book

import org.springframework.data.jpa.domain.Specification

class BookSpecification {
    companion object {
        fun withOwnerId(ownerId: Int?): Specification<Book> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Book>("owner").get<Int>("id"), ownerId)
            }
        }
    }
}
