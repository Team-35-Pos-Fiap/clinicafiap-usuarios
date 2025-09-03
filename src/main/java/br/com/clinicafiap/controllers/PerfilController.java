package br.com.clinicafiap.controllers;

import br.com.clinicafiap.entities.dto.PerfilDtoResponse;
import br.com.clinicafiap.services.interfaces.IPerfilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfis")
@Slf4j
public class PerfilController {

    private final IPerfilService perfilService;

    public PerfilController(IPerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<List<PerfilDtoResponse>> buscarTodos(){
        log.info("buscarTodos()");

        return ResponseEntity.ok(perfilService.buscarTodos());
    }
}
