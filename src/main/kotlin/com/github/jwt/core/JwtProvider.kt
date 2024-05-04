package com.github.jwt.core

import com.github.jwt.exception.jwtExceptionCatching
import com.github.jwt.security.JwtAuthentication
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.crypto.SecretKey

class JwtProvider(
    private val secretKey: SecretKey,
    private val accessTokenExpire: Long,
    private val refreshTokenExpire: Long
) {
    fun createAccessToken(authentication: JwtAuthentication): String =
        createToken(authentication.toClaims(), accessTokenExpire)

    fun createRefreshToken(authentication: JwtAuthentication): String =
        createToken(authentication.toClaims(), refreshTokenExpire)

    fun getAuthentication(token: String): JwtAuthentication =
        jwtExceptionCatching {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
                .toAuthentication()
        }

    fun createToken(claims: Claims, expire: Long): String =
        jwtExceptionCatching {
            val now = Date()

            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + expire))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
        }

    private fun JwtAuthentication.toClaims(): Claims =
        Jwts.claims(
            mapOf(
                "id" to id,
                "authorities" to authorities.joinToString(",") { it.authority }
            )
        )

    private fun Claims.toAuthentication(): JwtAuthentication =
        JwtAuthentication(
            id = get("id") as String,
            authorities = (get("authorities") as String)
                .split(",")
                .map(::SimpleGrantedAuthority)
                .toHashSet()
        )
}
