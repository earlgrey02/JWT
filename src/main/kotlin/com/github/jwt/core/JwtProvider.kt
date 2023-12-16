package com.github.jwt.core

import com.github.jwt.security.JwtAuthentication
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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

    private fun createToken(claims: Claims, expire: Long): String =
        Date().let {
            Jwts.builder()
                .setIssuedAt(it)
                .setExpiration(Date(it.time + expire))
                .setClaims(claims)
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
}