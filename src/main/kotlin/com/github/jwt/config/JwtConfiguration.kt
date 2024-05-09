package com.github.jwt.config

import com.github.jwt.core.DefaultJwtProvider
import com.github.jwt.core.JwtProvider
import com.github.jwt.util.minutesToMillis
import com.github.jwt.util.toSecretKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * [AutoConfiguration] class that registers [DefaultJwtProvider] as a Bean.
 * Class that is disabled when another [JwtProvider] exists as a Bean.
 */
@AutoConfiguration
@ConditionalOnMissingBean(JwtProvider::class)
class JwtConfiguration {
    /**
     * [DefaultJwtProvider] with properties injected.
     *
     * @param secret Secret string
     * @param accessTokenExpire Access token validity time(minute)
     * @param refreshTokenExpire Access token validity time(minute)
     */
    @Bean
    fun jwtProvider(
        @Value("\${jwt.secret}")
        secret: String,
        @Value("\${jwt.accessTokenExpire}")
        accessTokenExpire: Long,
        @Value("\${jwt.refreshTokenExpire}")
        refreshTokenExpire: Long
    ): DefaultJwtProvider = DefaultJwtProvider(
        secretKey = secret.toSecretKey(),
        accessTokenExpire = minutesToMillis(accessTokenExpire),
        refreshTokenExpire = minutesToMillis(refreshTokenExpire)
    )
}
