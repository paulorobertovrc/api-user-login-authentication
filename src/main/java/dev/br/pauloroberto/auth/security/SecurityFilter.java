package dev.br.pauloroberto.auth.security;

import dev.br.pauloroberto.auth.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        String subject;

        if (tokenService.isValid(token)) {
            // If token is valid, gets subject from token
            subject = tokenService.getSubject(token);
            // Gets user from database
            Optional<UserDetails> user = userRepository.findByUsername(subject);

            // If the user exists, an authentication object is created
            if (user.isEmpty()) throw new AssertionError("User not found");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.get(),
                    null,
                    user.get().getAuthorities());

            // Sets the authentication object in the context. This way, the user is authenticated
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // If token is valid, continues with the request. Else, the request is not processed
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
