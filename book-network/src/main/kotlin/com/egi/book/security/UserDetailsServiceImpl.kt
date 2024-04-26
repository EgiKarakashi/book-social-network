package com.egi.book.security

import com.egi.book.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    private val repository: UserRepository,
) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            repository.findByEmail(username)
                ?.orElseThrow { UsernameNotFoundException("User not found") }
        return user ?: throw UsernameNotFoundException("User not found")
    }
}
