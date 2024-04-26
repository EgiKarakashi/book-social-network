package com.egi.book.role

import com.egi.book.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Role(
    @Id
    @GeneratedValue
    private val id: Int = 0,
    @Column(unique = true)
    var name: String = "",
    @ManyToMany(mappedBy = "roles")
    private val users: List<User>? = null,
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private val createdDate: LocalDateTime? = LocalDateTime.now(),
    @LastModifiedDate
    @Column(insertable = false)
    private val lastModifiedDate: LocalDateTime? = LocalDateTime.now(),
)
