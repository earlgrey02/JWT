package com.github.jwt.core

import com.github.jwt.Token
import com.github.jwt.security.JwtAuthentication
import javax.crypto.SecretKey

/**
 * Specification of basic JWT functions.
 *
 * @throws [com.github.jwt.exception.JwtException]
 */
interface JwtProvider {
    /**
     * Method to generate token.
     *
     * @param header Header as a [Map]
     * @param payload Header as a [Map]
     * @param expire Token validity time(Milliseconds)
     * @return Token as a [String]
     */
    fun createToken(
        header: Map<String, String>,
        payload: Map<String, String>,
        expire: Long,
        secretKey: SecretKey
    ): Token

    /**
     * Method to generate token.
     *
     * @param payload Header as a [Map]
     * @param expire Token validity time(Milliseconds)
     * @return Token as a [String]
     */
    fun createToken(payload: Map<String, String>, expire: Long, secretKey: SecretKey): Token

    /**
     * Method to generate token.
     *
     * @param authentication Authentication as a [JwtAuthentication]
     * @param expire Token validity time(Milliseconds)
     * @return Token as a [String]
     */
    fun createToken(authentication: JwtAuthentication, expire: Long, secretKey: SecretKey): Token

    /**
     * Method to get header.
     *
     * @param token Token as a [String]
     * @return Header as a [Map]
     */
    fun getHeader(token: Token): Map<String, String>

    /**
     * Method to get payload.
     *
     * @param token Token as a [String]
     * @return Payload as a [Map]
     */
    fun getPayload(token: Token): Map<String, String>

    /**
     * Method to get authentication.
     * Implemented differently depending on the [JwtAuthentication] implementation used.
     *
     * @param token Token as a [String]
     * @return Authentication as a [JwtAuthentication]
     */
    fun getAuthentication(token: Token): JwtAuthentication
}
