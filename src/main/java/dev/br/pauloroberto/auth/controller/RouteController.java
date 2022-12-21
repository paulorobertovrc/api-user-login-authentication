package dev.br.pauloroberto.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@EnableWebSecurity
@EnableMethodSecurity
public class RouteController {

    @GetMapping("/home")
    public String home() {
        System.out.println("Home");
        return "GET / | PUBLIC";
    }

    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    @GetMapping("/home/user")
    public String user() {
        System.out.println("User");
        return "GET /user | ROLE_USER";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/home/admin")
    public String admin() {
        System.out.println("Admin");
        return "GET /admin | ROLE_ADMIN";
    }
}
