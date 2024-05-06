package com.github.jwt.security

import com.github.jwt.core.JwtProvider
import com.github.jwt.exception.JwtException
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorResume

/**
 * [WebFilter] that performs JWT authorization.
 * Register and use with [WebFilterChain] on Spring WebFlux
 *
 * @property jwtProvider [JwtProvider]
 */
class ReactiveJwtFilter(
    private val jwtProvider: JwtProvider
) : WebFilter {
    /**
     * This is a reactive way to perform JWT authentication.
     *
     * @param exchange HTTP request and response
     * @param chain
     * @return [Mono] returned by chain
     */
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith("Bearer ") }
            .switchIfEmpty(Mono.error(JwtException("Authorization header is invalid.")))
            .map { jwtProvider.getAuthentication(it.substring(7)) }
            .flatMap {
                chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
            }
            .onErrorResume(JwtException::class) { chain.filter(exchange) }
}
