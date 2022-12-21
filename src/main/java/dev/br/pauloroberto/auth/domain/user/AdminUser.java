package dev.br.pauloroberto.auth.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AdminUser extends User {
    public AdminUser() {
    }

    public AdminUser(Long id, String username, String password) {
        super(id, username, password, "ADMIN");
        new SimpleGrantedAuthority("ROLE_ADMIN");
    }
}
