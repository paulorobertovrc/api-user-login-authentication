package dev.br.pauloroberto.auth;

import dev.br.pauloroberto.auth.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiUserAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserAuthenticationApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository users) {
        return args -> {
//            users.save(new CommonUser(null, "user", "user"));
//            users.save(new AdminUser(null, "admin", "admin"));
//            System.out.println("user password INPUT: " + users.findByUsername("user").get().getPassword());
//            System.out.println(new BCryptPasswordEncoder().encode("user"));
        };
    }
}
