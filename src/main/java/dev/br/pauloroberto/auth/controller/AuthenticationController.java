package dev.br.pauloroberto.auth.controller;

import dev.br.pauloroberto.auth.domain.user.AuthenticationDto;
import dev.br.pauloroberto.auth.domain.user.User;
import dev.br.pauloroberto.auth.security.TokenDto;
import dev.br.pauloroberto.auth.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationDto authData) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authData.username(),
                authData.password()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // This is the user that was authenticated
        // getPrincipal() returns the user object that was authenticated
        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDto(token));
    }
}
