package com.egi.book.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TokenRepository : JpaRepository<Token, Int> {
    fun findByToken(token: String): Optional<Token>?
}
