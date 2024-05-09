package com.github.jwt.security

import com.github.jwt.core.DefaultJwtProvider
import com.github.jwt.util.toBearerToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * [OncePerRequestFilter] that performs JWT authorization.
 * Register and use with [SecurityFilterChain] on Spring Web MVC.
 *
 * @property jwtProvider [com.github.jwt.core.DefaultJwtProvider] which provides JWT functions
 */
class JwtFilter(
    private val jwtProvider: DefaultJwtProvider
) : OncePerRequestFilter() {
    /**
     * Method to perform authorization through bearer token.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader(HttpHeaders.AUTHORIZATION)
            .runCatching { jwtProvider.getAuthentication(toBearerToken()) }
            .onSuccess { SecurityContextHolder.getContext().authentication = it }
            .run { filterChain.doFilter(request, response) }
    }
}
