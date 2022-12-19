package dev.br.pauloroberto.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.br.pauloroberto.auth.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

// This class will handle the token generation and validation
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(User user) {
        // The token will be generated using the username and the secret
        try {
            // This is the secret key that will be used to sign the token
            // The same key is used both to generate and validate the signature
            // HMAC256 is the algorithm used to sign the token,
            // and it involves a combination of a hashing function and a secret key
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(Date.valueOf(LocalDate.now().plusDays(1)))
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error generating token");
        }
    }

    public String getSubject(String token) {
        // This method will be used to get the subject from the token (the username)
        // The token is signed, so we can validate the signature using the same secret key
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Error validating token");
        }
    }

    public boolean isValid(String token) {
        // This method will be used to validate the token
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
