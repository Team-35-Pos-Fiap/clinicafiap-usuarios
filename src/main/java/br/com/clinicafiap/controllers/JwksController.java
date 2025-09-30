package br.com.clinicafiap.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/auth/.well-known")
public class JwksController {

    private final KeyPair keyPair;
    private final String kid;

    public JwksController(KeyPair keyPair,
                          @Value("${security.jwt.kid}") String kid) {
        this.keyPair = keyPair;
        this.kid = kid;
    }

    @GetMapping(value = "/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> jwks() {
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        String n = b64Url(pub.getModulus());
        String e = b64Url(pub.getPublicExponent());
        var jwk = Map.of(
            "kty", "RSA",
            "kid", kid,
            "alg", "RS256",
            "use", "sig",
            "n", n,
            "e", e
        );
        return Map.of("keys", java.util.List.of(jwk));
    }

    private static String b64Url(BigInteger bi) {
        byte[] bytes = bi.toByteArray();
        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            bytes = tmp;
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
