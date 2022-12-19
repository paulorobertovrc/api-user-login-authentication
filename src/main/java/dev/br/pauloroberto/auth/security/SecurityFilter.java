package dev.br.pauloroberto.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        tokenService.getSubject(getToken(request));
        filterChain.doFilter(request, response);
    }

    private static String getToken(HttpServletRequest request) {
        String autorizathionHeader = request.getHeader("Authorization");
        if (autorizathionHeader == null || !autorizathionHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }

        return autorizathionHeader.replace("Bearer ", "");
    }
}
