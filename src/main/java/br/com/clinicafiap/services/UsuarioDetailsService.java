package br.com.clinicafiap.services;

import br.com.clinicafiap.entities.db.UsuarioDb;
import br.com.clinicafiap.repositories.UsuarioRepository;
import br.com.clinicafiap.security.UsuarioSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UsuarioDb usuario = usuarioRepository.recuperarDaodsUsuarioPorEmail(email);

        // Cada usuário só tem UM perfil
        String role = "ROLE_" + usuario.getPerfil().getNome().toUpperCase();

        return new UsuarioSecurity(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getSenha(),
            List.of(new SimpleGrantedAuthority(role))
        );
    }
}
