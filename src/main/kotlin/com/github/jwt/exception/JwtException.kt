package com.github.jwt.exception

data class JwtException(
    override val message: String?
) : RuntimeException(message)
