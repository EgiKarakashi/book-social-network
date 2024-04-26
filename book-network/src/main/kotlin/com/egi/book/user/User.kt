package com.egi.book.user

import com.egi.book.role.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @GeneratedValue
    val id: Int = 0,
    var firstname: String = "",
    private val lastname: String = "",
    private val dateOfBirth: LocalDate = LocalDate.now(),
    @Column(unique = true)
    val email: String = "",
    private val password: String = "",
    private val accountLocked: Boolean = false,
    var enabled: Boolean = false,
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private val roles: MutableList<Role?> = mutableListOf(),
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private val createdDate: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    @Column(insertable = false)
    private val lastModifiedDate: LocalDateTime = LocalDateTime.now(),
) : UserDetails, Principal {
    override fun getAuthorities(): MutableList<SimpleGrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it?.name) }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return !accountLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getName(): String {
        return email
    }

    fun fullName(): String {
        return "$firstname $lastname"
    }
}
