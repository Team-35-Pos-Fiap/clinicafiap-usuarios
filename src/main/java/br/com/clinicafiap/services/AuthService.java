package br.com.clinicafiap.services;

import br.com.clinicafiap.entities.db.UsuarioDb;
import br.com.clinicafiap.entities.dto.LoginDtoRequest;
import br.com.clinicafiap.entities.dto.TokenDtoResponse;
import br.com.clinicafiap.repositories.UsuarioRepository;
import br.com.clinicafiap.security.JwtSigner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtSigner jwtSigner;

    public AuthService(AuthenticationManager authManager,
                       UsuarioDetailsService usuarioDetailsService,
                       UsuarioRepository usuarioRepository,
                       JwtSigner jwtSigner) {
        this.authManager = authManager;
        this.usuarioDetailsService = usuarioDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.jwtSigner = jwtSigner;
    }

    public TokenDtoResponse autenticar(LoginDtoRequest loginDtoRequest) {
        autenticarCredenciais(loginDtoRequest.email(), loginDtoRequest.senha());
        UserDetails userDetails = carregarUserDetails(loginDtoRequest.email());
        UsuarioDb usuario = buscarUsuario(loginDtoRequest.email());
        String role = extrairRole(usuario);
        String token = gerarToken(userDetails, usuario, role);
        return montarResposta(token, usuario, role);
    }

    private void autenticarCredenciais(String email, String senha) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
    }

    private UserDetails carregarUserDetails(String email) {
        return usuarioDetailsService.loadUserByUsername(email);
    }

    private UsuarioDb buscarUsuario(String email) {
        return usuarioRepository.recuperarDaodsUsuarioPorEmail(email);
    }

    private String extrairRole(UsuarioDb usuario) {
        return "ROLE_" + usuario.getPerfil().getNome().toUpperCase();
    }

    private String gerarToken(UserDetails userDetails, UsuarioDb usuario, String role) {
        return jwtSigner.sign(
            userDetails.getUsername(),
            usuario.getId().toString(),
            List.of(role)
        );
    }

    private TokenDtoResponse montarResposta(String token, UsuarioDb usuario, String role) {
        return new TokenDtoResponse(token, usuario.getId().toString(), role);
    }
}