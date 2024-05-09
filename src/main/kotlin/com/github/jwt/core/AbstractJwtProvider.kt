package com.github.jwt.core

import com.github.jwt.Token
import com.github.jwt.exception.jwtExceptionCatching
import com.github.jwt.security.JwtAuthentication
import com.github.jwt.util.toMap
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import javax.crypto.SecretKey

/**
 * Specification for JWT functions including Secret Key.
 *
 * @property secretKey Secret Key as a [SecretKey]
 * @throws [com.github.jwt.exception.JwtException]
 */
abstract class AbstractJwtProvider(
    private val secretKey: SecretKey
) : JwtProvider {
    /**
     * Implementation of [JwtProvider.createToken].
     */
    override fun createToken(
        header: Map<String, String>,
        payload: Map<String, String>,
        expire: Long,
        secretKey: SecretKey
    ): Token =
        jwtExceptionCatching {
            Date().let {
                Jwts.builder()
                    .setHeader(header)
                    .setClaims(payload)
                    .setIssuedAt(it)
                    .setExpiration(Date(it.time + expire))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact()
            }
        }

    /**
     * Implementation of [JwtProvider.createToken].
     */
    override fun createToken(payload: Map<String, String>, expire: Long, secretKey: SecretKey): Token =
        createToken(emptyMap(), payload, expire, secretKey)

    /**
     * Implementation of [JwtProvider.createToken].
     */
    override fun createToken(authentication: JwtAuthentication, expire: Long, secretKey: SecretKey): Token =
        createToken(authentication.getPayload(), expire, secretKey)

    /**
     * Implementation of [JwtProvider.getHeader].
     */
    override fun getHeader(token: Token): Map<String, String> =
        jwtExceptionCatching {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .header
                .toMap()
        }

    /**
     * Implementation of [JwtProvider.getPayload].
     */
    override fun getPayload(token: Token): Map<String, String> =
        jwtExceptionCatching {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
                .toMap()
        }

    /**
     * Variant of [JwtProvider.createToken] using [secretKey].
     *
     * @param payload Payload as a [Map]
     * @param expire Token validity time(Milliseconds)
     * @return Token as a [String]
     */
    fun createToken(payload: Map<String, String>, expire: Long): Token =
        createToken(payload, expire, secretKey)

    /**
     * Variant of [JwtProvider.createToken] using [secretKey].
     *
     * @param authentication Authentication as a [JwtAuthentication]
     * @param expire Token validity time(Milliseconds)
     * @return Token as a [String]
     */
    fun createToken(authentication: JwtAuthentication, expire: Long): Token =
        createToken(authentication, expire, secretKey)
}
