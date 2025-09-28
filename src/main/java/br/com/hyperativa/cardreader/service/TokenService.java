package br.com.hyperativa.cardreader.service;

import br.com.hyperativa.cardreader.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String key;

    public String createToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("hyperativa-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpirationTime())
                    .sign(getAlgorithm());

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Something wrong during token creating.");
        }
    }

    public String validateToken(String token) {
        try {
            return JWT.require(getAlgorithm())
                    .withIssuer("hyperativa-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(key);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
