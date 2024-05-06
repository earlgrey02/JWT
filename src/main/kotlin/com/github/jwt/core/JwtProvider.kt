package com.github.jwt.core

import com.github.jwt.exception.jwtExceptionCatching
import com.github.jwt.security.JwtAuthentication
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import javax.crypto.SecretKey

/**
 * A class containing basic JWT functions.
 * Generally used as [org.springframework.context.annotation.Bean] by [com.github.jwt.config.JwtConfiguration].
 *
 * @property secretKey Secret key
 * @property accessTokenExpire Access token validity time(m)
 * @property refreshTokenExpire Access token validity time(m)
 */
class JwtProvider(
    private val secretKey: SecretKey,
    private val accessTokenExpire: Long,
    private val refreshTokenExpire: Long
) {
    /**
     * Method to create access token.
     *
     * @param authentication [JwtAuthentication] to contain in access token
     * @return Access token
     */
    fun createAccessToken(authentication: JwtAuthentication): String =
        createToken(authentication.toMap(), accessTokenExpire)

    /**
     * Method to create refresh token.
     *
     * @param authentication [JwtAuthentication] to contain in refresh token
     * @return Refresh token
     */
    fun createRefreshToken(authentication: JwtAuthentication): String =
        createToken(authentication.toMap(), refreshTokenExpire)

    /**
     * Method to get [JwtAuthentication] from token.
     *
     * @param token Access token or refresh token
     * @return [JwtAuthentication] used to generate token
     * @throws com.github.jwt.exception.JwtException
     */
    fun getAuthentication(token: String): JwtAuthentication =
        jwtExceptionCatching {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
                .toAuthentication()
        }

    /**
     * Method to common token.
     *
     * @param claims Token payload as [Map]
     * @param expire Token validity time(ms)
     * @return Token
     * @throws com.github.jwt.exception.JwtException
     */
    fun createToken(claims: Map<String, String>, expire: Long): String =
        jwtExceptionCatching {
            val now = Date()

            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + expire))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
        }

    /**
     * Method to common token.
     *
     * @param claims Token payload as [Claims]
     * @param expire Token validity time(ms)
     * @return Token
     * @throws com.github.jwt.exception.JwtException
     */
    private fun createToken(claims: Claims, expire: Long): String =
        jwtExceptionCatching {
            val now = Date()

            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + expire))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
        }

    /**
     * Method to get [Claims] from [JwtAuthentication].
     *
     * @receiver [JwtAuthentication]
     * @return [Claims]
     */
    private fun JwtAuthentication.toMap(): Map<String, String> =
        hashMapOf(
            "id" to id,
            "authorities" to authorities.joinToString(",") { it.authority }
        )

    /**
     * Method to get [JwtAuthentication] from [Claims].
     *
     * @receiver [Claims]
     * @return [JwtAuthentication]
     */
    private fun Claims.toAuthentication(): JwtAuthentication =
        JwtAuthentication(
            id = get("id") as String,
            authorities = (get("authorities") as String)
                .split(",")
                .map(::SimpleGrantedAuthority)
                .toHashSet()
        )
}
