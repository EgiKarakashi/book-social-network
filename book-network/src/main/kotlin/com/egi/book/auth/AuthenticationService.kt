package com.egi.book.auth

import com.egi.book.email.EmailService
import com.egi.book.email.EmailTemplateName
import com.egi.book.role.RoleRepository
import com.egi.book.security.JwtService
import com.egi.book.user.Token
import com.egi.book.user.TokenRepository
import com.egi.book.user.User
import com.egi.book.user.UserRepository
import jakarta.mail.MessagingException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationService(
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val emailService: EmailService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    @Value("\${application.mailing.frontend.activation-url}")
    private val activationUrl: String,
) {
    @Throws(MessagingException::class, IllegalStateException::class)
    fun register(request: RegistrationRequest) {
        val userRole =
            roleRepository.findByName("USER")
                ?.orElseThrow { IllegalStateException("ROLE USER was not initiated") }

        val user =
            User(
                firstname = request.firstname,
                lastname = request.lastname,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                accountLocked = false,
                enabled = false,
                roles = mutableListOf(userRole),
            )
        userRepository.save(user)
        sendValidationEmail(user)
    }

    fun generateAndSaveActivationToken(user: User): String {
        val generatedToken = generateActivationCode(6)
        val token =
            Token(
                token = generatedToken,
                createdAt = LocalDateTime.now(),
                expiresAt = LocalDateTime.now().plusMinutes(15),
                user = user,
            )
        tokenRepository.save(token)
        return generatedToken
    }

    @Throws(MessagingException::class)
    fun sendValidationEmail(user: User) {
        val newToken = generateAndSaveActivationToken(user)
        emailService.sendEmail(
            user.email,
            user.fullName(),
            EmailTemplateName.ACTIVATE_ACCOUNT,
            activationUrl,
            newToken,
            "Account activation",
        )
    }

    private fun generateActivationCode(length: Int): String {
        val characters = "0123456789"
        val codeBuilder = StringBuilder()

        val secureRandom = SecureRandom()

        repeat(length) {
            val randomIndex = secureRandom.nextInt(characters.length)
            codeBuilder.append(characters[randomIndex])
        }

        return codeBuilder.toString()
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse? {
        val auth =
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.email,
                    request.password,
                ),
            )
        val claims = hashMapOf<String, Any>()
        val user = (auth.principal as User)
        claims["fullName"] = user.fullName()

        val jwtToken = jwtService.generateToken(claims, user)
        // apply used only for large data
        return AuthenticationResponse().apply {
            this.token = jwtToken
        }
    }

    fun activateAccount(token: String) {
        val savedToken =
            tokenRepository.findByToken(token)
                ?.orElseThrow { RuntimeException("Invalid token") }

        if (LocalDateTime.now().isAfter(savedToken?.expiresAt)) {
            savedToken?.user?.let { sendValidationEmail(it) }
            throw RuntimeException("Activation token has expired. A new token has been sent")
        }
        val user =
            savedToken?.user?.id?.let { userRepository.findById(it) }
                ?.orElseThrow { UsernameNotFoundException("User not found") }
        user?.enabled = true
        if (user != null) {
            userRepository.save(user)
        }
        savedToken?.validatedAt = LocalDateTime.now()
        if (savedToken != null) {
            tokenRepository.save(savedToken)
        }
    }
}
