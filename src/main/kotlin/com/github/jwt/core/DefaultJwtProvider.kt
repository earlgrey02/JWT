package com.github.jwt.core

import com.github.jwt.Token
import com.github.jwt.security.DefaultJwtAuthentication
import com.github.jwt.security.JwtAuthentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.crypto.SecretKey

/**
 * Implementation of [AbstractJwtProvider] with functions related to JWT authentication.
 * The JWT authentication function is implemented based on Default JWT Authentication.
 *
 * @property secretKey Secret Key as a [SecretKey]
 * @property accessTokenExpire Access token validity time(milliseconds)
 * @property refreshTokenExpire Refresh token validity time(milliseconds)
 * @throws [com.github.jwt.exception.JwtException]
 */
class DefaultJwtProvider(
    secretKey: SecretKey,
    private val accessTokenExpire: Long,
    private val refreshTokenExpire: Long
) : AbstractJwtProvider(secretKey) {
    /**
     * Method to generate access token.
     *
     * @param authentication Authentication as a [JwtAuthentication]
     * @return Token as a [String]
     */
    fun createAccessToken(authentication: JwtAuthentication): Token =
        createToken(authentication, accessTokenExpire)

    /**
     * Method to generate refresh token.
     *
     * @param authentication Authentication as a [JwtAuthentication]
     * @return Token as a [String]
     */
    fun createRefreshToken(authentication: JwtAuthentication): Token =
        createToken(authentication, refreshTokenExpire)

    /**
     * Method to perform refresh through refresh token.
     *
     * @param refreshToken Refresh token as a [String]
     * @return Access token and refresh token as a [Pair]
     */
    fun refresh(refreshToken: Token): Pair<Token, Token> =
        getAuthentication(refreshToken)
            .let { createAccessToken(it) to createRefreshToken(it) }

    /**
     * Implementation of [JwtProvider.getAuthentication].
     * Returns [DefaultJwtAuthentication].
     */
    override fun getAuthentication(token: Token): DefaultJwtAuthentication =
        getPayload(token)
            .let {
                DefaultJwtAuthentication(
                    id = it["id"]!!,
                    roles = it["roles"]!!
                        .split(",")
                        .map(::SimpleGrantedAuthority)
                        .toHashSet()
                )
            }
}
