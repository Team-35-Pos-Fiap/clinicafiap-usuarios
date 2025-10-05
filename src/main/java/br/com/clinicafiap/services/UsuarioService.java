package br.com.clinicafiap.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.clinicafiap.entities.db.UsuarioDb;
import br.com.clinicafiap.entities.domain.Usuario;
import br.com.clinicafiap.entities.dto.PerfilDtoResponse;
import br.com.clinicafiap.entities.dto.UsuarioDtoRequest;
import br.com.clinicafiap.entities.dto.UsuarioDtoResponse;
import br.com.clinicafiap.mappers.UsuarioMapper;
import br.com.clinicafiap.repositories.interfaces.IUsuarioRepository;
import br.com.clinicafiap.services.exceptions.EmailDuplicadoException;
import br.com.clinicafiap.services.interfaces.IPerfilService;
import br.com.clinicafiap.services.interfaces.IUsuarioService;
import br.com.clinicafiap.utils.MensagensUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService implements IUsuarioService, ApplicationRunner {

	private final IUsuarioRepository usuarioRepository;
	private final IPerfilService perfilService;
	private final PasswordEncoder passwordEncoder;
	
	private final CacheManager cacheManager;
	
	private final String CACHE_USUARIOS_POR_PERFIL = "usuarios_por_perfil";
	private final String CACHE_USUARIO = "usuario";

	public UsuarioService(IUsuarioRepository usuarioRepository, CacheManager cacheManager, IPerfilService perfilService, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.cacheManager = cacheManager;
		this.perfilService = perfilService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@CircuitBreaker(name = "usuarios", fallbackMethod = "recuperarUsuarioNoCache")
	@Retry(name = "usuarios", fallbackMethod = "recuperarUsuarioNoCache")	
	public UsuarioDtoResponse buscarPorId(UUID id) {
		log.info("busca por id {}", id);
		
		return toUsuarioDto(toUsuario(buscarUsuarioPorId(id)));
	}

	@Override
	@CircuitBreaker(name = "usuarios", fallbackMethod = "recuperarUsuariosNoCache")
	@Retry(name = "usuarios", fallbackMethod = "recuperarUsuariosNoCache")
	public List<UsuarioDtoResponse> buscarUsuariosPorPerfil(Integer idPerfil) {
		return toListUsuarioDtoResponse(buscarUsuarios(idPerfil));
	}
	
	@Override
	public UsuarioDtoResponse cadastrar(UsuarioDtoRequest usuario) {
		verificaEmailCadastrado(usuario.email());

		salvar(toUsuario(usuario));

		var novoUsuario = usuarioRepository.recuperarDaodsUsuarioPorEmail(usuario.email());
		return toUsuarioDto(toUsuario(novoUsuario));
	}

	@Override
	public void atualizarStatus(UUID id, boolean isAtivo) {
		UsuarioDb usuario = buscarUsuarioPorId(id);
		
		if(isAtivo) { 
			usuario.reativar();
		} else {
			usuario.inativar();
		}

		salvar(usuario);
	}

	@Override
	public void atualizarNome(UUID id, String nome) {
		UsuarioDb usuario = buscarUsuarioPorId(id);

		usuario.atualizarNome(nome);
			
		salvar(usuario);
	}

	@Override
	public void atualizarEmail(UUID id, String email) {
		UsuarioDb usuario = buscarUsuarioPorId(id);
		
		verificaEmailCadastrado(email);
		
		usuario.atualizarEmail(email);
		
		salvar(usuario);
	}
	
	@Override
	public void atualizarSenha(UUID id, String senha) {
		UsuarioDb usuario = buscarUsuarioPorId(id);
		
		usuario.atualizarSenha(senha);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		salvar(usuario);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<PerfilDtoResponse> perfis = carregarPerfis();
		
		perfis.stream().forEach(p -> buscarUsuarios(p.id()));
	}
	
	private UsuarioDb buscarUsuarioPorId(UUID id) {
		return usuarioRepository.recuperaDadosUsuarioPorId(id);
	}
	
	private List<UsuarioDtoResponse> toListUsuarioDtoResponse(List<UsuarioDb> usuarios) {
		return UsuarioMapper.toListUsuarioDtoResponse(usuarios);
	}

	private List<UsuarioDtoResponse> recuperarUsuariosNoCache(Integer idPerfil, Throwable t) {
		log.info("Método de fallback da busca de usuários no perfil {}.", idPerfil);

		return toListUsuarioDtoResponse(recuperarUsuariosNoCache(idPerfil));
	}
	
	private UsuarioDtoResponse recuperarUsuarioNoCache(UUID id, Throwable t) {
		log.info("Método de fallback da busca de usuário pelo id {}.", id);

		validaRetornoFallback(t);

		return toUsuarioDto(toUsuario(recuperarUsuarioNoCache(id)));
	}
	
	private UsuarioDb recuperarUsuarioNoCache(UUID id) {
		return cacheManager.getCache(CACHE_USUARIO).get(id, UsuarioDb.class);
	}
	
	private List<UsuarioDb> recuperarUsuariosNoCache(Integer idPerfil) {
		return cacheManager.getCache(CACHE_USUARIOS_POR_PERFIL).get(idPerfil, ArrayList.class);
	}
	
	private void verificaEmailCadastrado(String email) {
		boolean possuiEmailCadastrado = usuarioRepository.emailJaCadastrado(email);
		
		if(possuiEmailCadastrado){
			throw new EmailDuplicadoException(MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_EMAIL_DUPLICADO));
		}
	}
	
	private List<UsuarioDb> buscarUsuarios(Integer idPerfil) {
		return usuarioRepository.buscarUsuariosPorPerfil(idPerfil);
	}

	private void salvar(Usuario usuario) {
		usuarioRepository.salvar(toUsuarioDb(usuario));
	}

	private void salvar(UsuarioDb usuario) {

		usuarioRepository.salvar(usuario);
	}
	
	private List<PerfilDtoResponse> carregarPerfis() {
		return perfilService.buscarTodos();
	}
	
	private Usuario toUsuario(UsuarioDb usuario) {
		return UsuarioMapper.toUsuario(usuario);
	}
	
	private UsuarioDb toUsuarioDb(Usuario usuario) {
		return UsuarioMapper.toUsuarioDb(usuario);
	}

	private UsuarioDtoResponse toUsuarioDto(Usuario usuario) {
		return UsuarioMapper.toUsuarioDtoResponse(usuario);
	}
	
	private Usuario toUsuario(UsuarioDtoRequest usuario) {
		return UsuarioMapper.toUsuario(usuario, passwordEncoder);
	}

	private void validaRetornoFallback(Throwable throwable) {
		if (throwable instanceof br.com.clinicafiap.services.exceptions.UsuarioNaoEncontradoException
				|| throwable instanceof br.com.clinicafiap.services.exceptions.PerfilNaoEncontradoException
				|| throwable instanceof br.com.clinicafiap.services.exceptions.EmailDuplicadoException
				|| throwable instanceof IllegalArgumentException
				|| throwable instanceof jakarta.validation.ConstraintViolationException) {
			if (throwable instanceof RuntimeException re) throw re;
			throw new RuntimeException(throwable);
		}
	}
}