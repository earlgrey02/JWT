package com.github.jwt.exception

/**
 * Exception thrown by [jwtExceptionCatching].
 * Common exceptions used in [com.github.jwt.core.JwtProvider].
 *
 * @property message Error message
 */
data class JwtException(
    override val message: String?
) : RuntimeException(message)
