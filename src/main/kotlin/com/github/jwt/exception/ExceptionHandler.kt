package com.github.jwt.exception

fun <T> jwtExceptionCatching(init: () -> T) =
    runCatching(init)
        .onFailure { throw JwtException(it.message) }
        .getOrThrow()
