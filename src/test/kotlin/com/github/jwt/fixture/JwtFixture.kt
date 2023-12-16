package com.github.jwt.fixture

import com.github.jwt.config.JwtConfiguration
import com.github.jwt.security.JwtAuthentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

val SECRET_KEY = (1..100).map { ('a'..'z').random() }.joinToString("")
const val ACCESS_TOKEN_EXPIRE = 1L
const val REFRESH_TOKEN_EXPIRE = 10L
const val ID = "test"
val AUTHORITIES = setOf(SimpleGrantedAuthority("USER"))
const val INVALID_TOKEN = "test"
val jwtProvider = JwtConfiguration().jwtProvider(
    secretKey = SECRET_KEY,
    accessTokenExpire = ACCESS_TOKEN_EXPIRE,
    refreshTokenExpire = REFRESH_TOKEN_EXPIRE
)

fun createJwtAuthentication(
    id: String = ID,
    authorities: Set<GrantedAuthority> = AUTHORITIES,
): JwtAuthentication =
    JwtAuthentication(
        id = id,
        authorities = authorities,
    )