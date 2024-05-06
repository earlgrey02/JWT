package com.github.jwt.fixture

import com.github.jwt.security.JwtAuthentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

val SECRET_KEY = (1..100).map { ('a'..'z').random() }.joinToString("")
const val ACCESS_TOKEN_EXPIRE = 1L
const val REFRESH_TOKEN_EXPIRE = 10L
const val ID = "id"
val AUTHORITIES = setOf(SimpleGrantedAuthority("USER"))
const val INVALID_TOKEN = "invalid_token"

fun createJwtAuthentication(
    id: String = ID,
    authorities: Set<GrantedAuthority> = AUTHORITIES,
): JwtAuthentication =
    JwtAuthentication(
        id = id,
        authorities = authorities,
    )
