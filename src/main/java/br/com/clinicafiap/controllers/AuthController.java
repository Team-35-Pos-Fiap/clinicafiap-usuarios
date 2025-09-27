package br.com.clinicafiap.controllers;

import br.com.clinicafiap.entities.dto.LoginDtoRequest;
import br.com.clinicafiap.entities.dto.TokenDtoResponse;
import br.com.clinicafiap.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDtoResponse> login(@RequestBody @Valid LoginDtoRequest loginDtoRequest) {
        TokenDtoResponse resposta = authService.autenticar(loginDtoRequest);
        return ResponseEntity.ok(resposta);
    }
}
