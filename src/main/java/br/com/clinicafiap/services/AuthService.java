package br.com.clinicafiap.services;

import br.com.clinicafiap.entities.dto.LoginDtoRequest;
import br.com.clinicafiap.entities.dto.TokenDtoResponse;
import br.com.clinicafiap.repositories.UsuarioRepository;
import br.com.clinicafiap.security.JwtSigner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        String email = loginDtoRequest.email();
        String senha = loginDtoRequest.senha();

        // 1) Autentica credenciais
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));

        // 2) Carrega detalhes do usuário
        var userDetails = usuarioDetailsService.loadUserByUsername(email);
        var usuario = usuarioRepository.recuperarDaodsUsuarioPorEmail(email);

        // 3) Extrai role a partir do Perfil
        String role = "ROLE_" + usuario.getPerfil().getNome().toUpperCase();

        // 4) Gera JWT
        String token = jwtSigner.sign(
            userDetails.getUsername(),
            usuario.getId().toString(),
            List.of(role)
        );

        // 5) Retorna DTO
        return new TokenDtoResponse(token, usuario.getId().toString(), role);
    }
}
