package br.com.clinicafiap.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class UsuarioSecurity extends User {
    private final UUID uid;

    public UsuarioSecurity(UUID uid, String username, String password,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.uid = uid;
    }

    public UUID getUid() {
        return uid;
    }
}
