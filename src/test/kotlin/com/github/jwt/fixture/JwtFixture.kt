package com.github.jwt.fixture

import com.github.jwt.security.DefaultJwtAuthentication
import com.github.jwt.security.JwtAuthentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

val SECRET = (1..100).map { ('a'..'z').random() }.joinToString("")
const val ACCESS_TOKEN_EXPIRE = 1L
const val REFRESH_TOKEN_EXPIRE = 10L
const val ID = "id"
val ROLES = setOf(SimpleGrantedAuthority("USER"))
const val INVALID_TOKEN = "invalid_token"

fun createDefaultJwtAuthentication(
    id: String = ID,
    roles: Set<GrantedAuthority> = ROLES,
): JwtAuthentication =
    DefaultJwtAuthentication(
        id = id,
        roles = roles
    )
