package com.egi.book.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseEntity(
    @Id
    @GeneratedValue
    var id: Int,
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private val createdDate: LocalDateTime,
    @LastModifiedDate
    @Column(insertable = false)
    private val lastModifiedDate: LocalDateTime,
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private val createdBy: Int,
    @LastModifiedBy
    @Column(insertable = false)
    private val lastModifiedBy: Int,
)
