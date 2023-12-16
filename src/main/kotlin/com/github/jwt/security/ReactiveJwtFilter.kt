package com.github.jwt.security

import com.github.jwt.core.JwtProvider
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

class ReactiveJwtFilter(
    private val jwtProvider: JwtProvider
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
            .filter { it.startsWith("Bearer ") }
            .switchIfEmpty { Mono.error(IllegalArgumentException("Authorization header is invalid.")) }
            .map { jwtProvider.getAuthentication(it) }
            .flatMap {
                chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(it))
            }
            .onErrorResume { chain.filter(exchange) }
}