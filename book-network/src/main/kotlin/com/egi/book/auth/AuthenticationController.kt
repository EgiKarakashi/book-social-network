package com.egi.book.auth

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.mail.MessagingException
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Throws(MessagingException::class, IllegalStateException::class)
    fun register(
        @RequestBody @Valid request: RegistrationRequest,
    ): ResponseEntity<*> {
        authenticationService.register(request)

        return ResponseEntity.accepted().build<Any>()
    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody request: AuthenticationRequest,
    ): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.authenticate(request)
//        return ResponseEntity.ok(AuthenticationResponse(response!!.token))
        return ResponseEntity.ok(response)
    }

    @GetMapping("/activate-account")
    @Throws(MessagingException::class)
    fun confirm(
        @RequestParam token: String,
    ) {
        authenticationService.activateAccount(token)
    }

    companion object {
        private val log = LoggerFactory.getLogger(AuthenticationController::class.java)
    }
}
