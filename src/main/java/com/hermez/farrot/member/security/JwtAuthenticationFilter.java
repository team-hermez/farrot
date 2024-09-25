package com.hermez.farrot.member.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        if(token != null) {
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("JWT token is invalid");
                throw new IllegalArgumentException("JWT token is invalid");
            }

            Claims info = jwtTokenProvider.parseClaims(token);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request, response);
    }
    public void setAuthentication(String email) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtTokenProvider.createAuthentication(email);
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}
