package com.github.jwt.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class JwtAuthentication(
    val id: String,
    @JvmField
    val authorities: Set<GrantedAuthority>
) : Authentication {
    override fun getAuthorities(): Set<GrantedAuthority> = authorities

    override fun getName(): String? = null

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any? = null

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw UnsupportedOperationException("Cannot change the authenticated state of JwtAuthentication")
    }
}