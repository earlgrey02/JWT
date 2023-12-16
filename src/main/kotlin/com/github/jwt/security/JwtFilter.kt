package com.github.jwt.security

import com.github.jwt.core.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (header.startsWith("Bearer ")) {
            val authentication = jwtProvider.getAuthentication(header.substring(7))

            SecurityContextHolder.getContext().authentication = authentication
        }

        return filterChain.doFilter(request, response)
    }
}