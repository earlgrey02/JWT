package com.github.jwt.exception

/**
 * Exception thrown by [jwtExceptionCatching].
 */
data class JwtException(
    override val message: String?
) : RuntimeException(message)
