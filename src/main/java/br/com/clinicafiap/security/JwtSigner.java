package br.com.clinicafiap.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtSigner {

    private final KeyPair keyPair;
    private final String issuer;
    private final long expiresMin;
    private final String kid;

    public JwtSigner(KeyPair keyPair,
                     @Value("${security.jwt.issuer}")String issuer,
                     @Value("${security.jwt.expires-min}")long expiresMin,
                     @Value("${security.jwt.kid}")String kid) {
        this.keyPair = keyPair;
        this.issuer = issuer;
        this.expiresMin = expiresMin;
        this.kid = kid;
    }

    public String sign(String username, String uid, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("kid", kid)
            .setIssuer(issuer)
            .setSubject(username)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(expiresMin * 60)))
            .claim("uid", uid.toString())
            .claim("roles", roles)
            .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
            .compact();
    }
}
