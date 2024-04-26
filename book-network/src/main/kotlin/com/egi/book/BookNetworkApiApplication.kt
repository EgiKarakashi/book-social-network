package com.egi.book

import com.egi.book.role.Role
import com.egi.book.role.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
class BookNetworkApiApplication {
    @Bean
    fun runner(roleRepository: RoleRepository) =
        CommandLineRunner {
            val userRole = roleRepository.findByName("USER")

            if (userRole?.isEmpty == true) {
                val newUserRole = Role(name = "USER")
                roleRepository.save(newUserRole)
            }
        }
}

fun main(args: Array<String>) {
    runApplication<BookNetworkApiApplication>(*args)
}
