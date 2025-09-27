package br.com.clinicafiap.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Configuration
public class JwtKeyConfig {

    @Bean
    public KeyPair jwtKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }
}
// Obs: Toda vez que o container subir, um novo par de chaves e criado. Tokens antigos deixam de valer
