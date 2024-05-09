package com.github.jwt.security

import org.springframework.security.core.GrantedAuthority

/**
 * Implementation of [JwtAuthentication].
 *
 * @property id Identifier of the authentication subject as a [String]
 */
data class DefaultJwtAuthentication(
    val id: String,
    override val roles: Set<GrantedAuthority>
) : JwtAuthentication(roles) {
    /**
     * Implementation of [JwtAuthentication.getPayload].
     *
     * @return Payload as a [Map]
     */
    override fun getPayload(): Map<String, String> =
        hashMapOf(
            "id" to id,
            "roles" to roles.joinToString(",") { it.authority }
        )
}
