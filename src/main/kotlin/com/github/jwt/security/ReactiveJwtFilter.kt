package com.github.jwt.security

import com.github.jwt.core.DefaultJwtProvider
import com.github.jwt.exception.JwtException
import com.github.jwt.util.toBearerToken
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume

/**
 * [WebFilter] that performs JWT authorization.
 * Register and use with [SecurityWebFilterChain] on Spring Reactive Security.
 *
 * @property jwtProvider [com.github.jwt.core.DefaultJwtProvider] which provides JWT functions
 */
class ReactiveJwtFilter(
    private val jwtProvider: DefaultJwtProvider
) : WebFilter {
    /**
     * Method to perform authorization through bearer token.
     *
     * @param exchange
     * @param chain
     */
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        Mono.just(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: "")
            .map { jwtProvider.getAuthentication(it.toBearerToken()) }
            .flatMap {
                chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
            }
            .onErrorResume(JwtException::class) { chain.filter(exchange) }
}
