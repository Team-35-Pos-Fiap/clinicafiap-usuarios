package br.com.clinicafiap.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

@Component
public class JwtValidator {

    private final KeyPair keyPair;
    private final String expectedIssuer;

    public JwtValidator(KeyPair keyPair,
                        @Value("${security.jwt.issuer:usuarios-service}") String expectedIssuer) {
        this.keyPair = keyPair;
        this.expectedIssuer = expectedIssuer;
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
            .requireIssuer(expectedIssuer)
            .setSigningKey(keyPair.getPublic())
            .build()
            .parseClaimsJws(token);
    }
}
