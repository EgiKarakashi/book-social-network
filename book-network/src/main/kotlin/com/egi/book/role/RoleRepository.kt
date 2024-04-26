package com.egi.book.role

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: String): Optional<Role>?
}
