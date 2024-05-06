package com.github.jwt.exception

/**
 * Exception handling method that converts exceptions to [JwtException].
 *
 * @param init Lambda to handle exception
 * @return Return value of lambda
 * @throws JwtException
 */
internal fun <T> jwtExceptionCatching(init: () -> T): T =
    runCatching(init)
        .onFailure { throw JwtException(it.message) }
        .getOrThrow()
