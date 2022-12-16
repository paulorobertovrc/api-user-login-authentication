package dev.br.pauloroberto.auth.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // This method's name needs to be "findBy" + the field name
    Optional<UserDetails> findByUsername(String username);
}
