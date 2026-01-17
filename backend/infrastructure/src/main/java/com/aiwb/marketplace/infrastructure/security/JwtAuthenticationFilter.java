package com.aiwb.marketplace.infrastructure.security;

import com.aiwb.marketplace.application.auth.TokenPayload;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.domain.user.RoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                TokenPayload payload = tokenService.parseAccessToken(token);
                Set<SimpleGrantedAuthority> authorities = payload.roles().stream()
                        .map(RoleType::name)
                        .map(role -> "ROLE_" + role)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        payload.email(),
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (RuntimeException ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
