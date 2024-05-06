package com.github.jwt.config

import com.github.jwt.core.JwtProvider
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import java.util.concurrent.TimeUnit

/**
 * [AutoConfiguration] class that registers [JwtProvider] as a [Bean].
 */
@AutoConfiguration
class JwtConfiguration {
    /**
     * [JwtProvider] with properties injected.
     *
     * @param secretKey Secret key
     * @param accessTokenExpire Access token validity time(m)
     * @param refreshTokenExpire Access token validity time(m)
     */
    @Bean
    fun jwtProvider(
        @Value("\${jwt.secretKey}")
        secretKey: String,
        @Value("\${jwt.accessTokenExpire}")
        accessTokenExpire: Long,
        @Value("\${jwt.refreshTokenExpire}")
        refreshTokenExpire: Long
    ): JwtProvider = JwtProvider(
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
        accessTokenExpire = TimeUnit.MINUTES.toMillis(accessTokenExpire),
        refreshTokenExpire = TimeUnit.MINUTES.toMillis(refreshTokenExpire)
    )
}
