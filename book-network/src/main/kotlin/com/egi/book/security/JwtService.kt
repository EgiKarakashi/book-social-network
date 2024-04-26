package com.egi.book.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService(
    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration: Long = 0,
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String = "",
) {
    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(hashMapOf<String, Any>(), userDetails)
    }

    fun generateToken(
        claims: Map<String, Any>,
        userDetails: UserDetails,
    ): String {
        return buildToken(claims, userDetails, jwtExpiration)
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        jwtExpiration: Long,
    ): String {
        val authorities =
            userDetails.authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()

        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
            .claim("authorities", authorities)
            .signWith(getSignInKey())
            .compact()
    }

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        val keyBytes =
            try {
                Base64.getDecoder().decode(secretKey)
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid base64 encoded secretKey: $secretKey")
            }

        if (keyBytes.isEmpty()) {
            throw IllegalArgumentException("Secret key is empty")
        }

        return Keys.hmacShaKeyFor(keyBytes)
    }
}
