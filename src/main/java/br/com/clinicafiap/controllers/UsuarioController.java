package br.com.clinicafiap.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.clinicafiap.controllers.response.MensagemResponse;
import br.com.clinicafiap.entities.dto.EmailDtoRequest;
import br.com.clinicafiap.entities.dto.NomeDtoRequest;
import br.com.clinicafiap.entities.dto.SenhaDtoRequest;
import br.com.clinicafiap.entities.dto.UsuarioDtoRequest;
import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;
import br.com.clinicafiap.services.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

	private IUsuarioService usuarioService;

	public UsuarioController(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	public ResponseEntity<Void> cadastrar(@Valid @RequestBody @NotNull UsuarioDtoRequest usuario) {
		log.info("cadastrar():dados do usuário {}", usuario);
		
		usuarioService.cadastrar(usuario);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{id}/status/inativa")
	public ResponseEntity<MensagemResponse> inativar(@Valid @PathVariable @NotNull UUID id) {
		log.info("inativar():id {}", id);

		usuarioService.atualizarStatus(id, false);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}
	
	@PatchMapping("/{id}/status/reativa")
	public ResponseEntity<MensagemResponse> reativar(@Valid @PathVariable @NotNull UUID id) {
		log.info("reativar():id {}", id);

		usuarioService.atualizarStatus(id, true);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDtoResponse> buscarUsuarioPorId(@PathVariable @NotNull @Valid UUID id) {
		log.info("buscarUsuarioPorId() - id {}", id);
		
		return ResponseEntity.ok().body(usuarioService.buscarPorId(id));
	}
	
	@GetMapping("/perfil/{id}")
	public ResponseEntity<List<UsuarioDtoResponse>> buscarUsuariosPorPerfil(@PathVariable(name = "id") @NotNull @Valid Integer idPerfil) {
		log.info("buscarUsuarios() - perfil: {}", idPerfil);

		return ResponseEntity.ok().body(usuarioService.buscarUsuariosPorPerfil(idPerfil));
	}

	@PatchMapping("/{id}/nome")
	public ResponseEntity<Void> atualizarNome(@Valid @PathVariable @NotNull UUID id, @Valid @RequestBody @NotNull NomeDtoRequest dados) {
		usuarioService.atualizarNome(id, dados.nome());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}

	@PatchMapping("/{id}/email")
	public ResponseEntity<Void> atualizarEmail(@Valid @PathVariable @NotNull UUID id, @Valid @RequestBody @NotNull EmailDtoRequest dados) {
		usuarioService.atualizarEmail(id, dados.email());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}
	
	@PatchMapping("/{id}/senha")
	public ResponseEntity<Void> atualizarSenha(@Valid @PathVariable @NotNull UUID id, @Valid @RequestBody @NotNull SenhaDtoRequest dados) {
		usuarioService.atualizarSenha(id, dados.senha());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}
}