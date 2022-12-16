package dev.br.pauloroberto.auth.controller;

import dev.br.pauloroberto.auth.domain.user.AuthenticationDto;
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

    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationDto authData) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authData.username(), authData.password());
        Authentication authentication = authenticationManager.authenticate(token);


        return ResponseEntity.ok().build();
    }
}
