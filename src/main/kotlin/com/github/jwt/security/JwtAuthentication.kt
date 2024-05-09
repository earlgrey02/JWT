package com.github.jwt.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

/**
 * Specification of authentication object used for JWT authentication and authorization.
 *
 * @throws [com.github.jwt.exception.JwtException]
 */
abstract class JwtAuthentication(
    open val roles: Set<GrantedAuthority>
) : Authentication {
    override fun getName(): String {
        throw UnsupportedOperationException("JwtAuthentication does not include name.")
    }

    override fun getAuthorities(): Set<GrantedAuthority> = roles

    override fun getCredentials() {
        throw UnsupportedOperationException("JwtAuthentication does not support credential.")
    }

    override fun getDetails() {
        throw UnsupportedOperationException("JwtAuthentication does not support details.")
    }

    override fun getPrincipal() {
        throw UnsupportedOperationException("JwtAuthentication does not support principal.")
    }

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw UnsupportedOperationException("Cannot change the authenticated state of JwtAuthentication")
    }

    /**
     * Method used to retrieve the payload to be used for token creation.
     *
     * @return Payload as a [Map]
     */
    abstract fun getPayload(): Map<String, String>
}
